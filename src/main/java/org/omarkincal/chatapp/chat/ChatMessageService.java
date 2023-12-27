package org.omarkincal.chatapp.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omarkincal.chatapp.chatroom.ChatRoomService;
import org.omarkincal.chatapp.user.UserService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService{
    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;
    private final UserService userService;

/*
    {
        try {
            secretKey = KeyGenerator.getInstance("AES").generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
*/
    /*
    * String plainText = "Bu bir blok şifreleme örneğidir.";

            // Anahtar oluştur


            // Şifreleme nesnesi oluştur
            Cipher cipher = Cipher.getInstance("AES");

            // Metni şifrele
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

            // Şifrelenmiş metni yazdır
            System.out.println("Şifrelenmiş Metin: " + Base64.getEncoder().encodeToString(encryptedBytes));

            System.out.println(encryptedBytes);

            // Şifrelenmiş metni deşifre et
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            System.out.println(decryptedBytes.toString());

            // Deşifre edilmiş metni yazdır
            System.out.println("Deşifre Edilmiş Metin: " + new String(decryptedBytes));
    *
    * */

    public ChatMessage saveMessage(ChatMessage message) {
        try {
           String chatId = chatRoomService.getChatRoomId(
                            message.getSenderId(),
                            message.getRecipientId(),
                            true)
                    .orElseThrow();


            message.setChatId(chatId);


            // Mesajı şifrele
            String encryptedMessage = MessageEncryption.encryptMessage(message.getContent(),chatId );
            message.setContent(encryptedMessage);

            repository.save(message);

            String decryptedMessage = MessageEncryption.decryptMessage(message.getContent(),chatId );
            message.setContent(decryptedMessage);

            return message;
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
            return null;
        }
    }

  /*  public ChatMessage saveMessage(ChatMessage message){
        var chatId = chatRoomService.getChatRoomId(
                message.getSenderId(),
                message.getRecipientId(),
                true)
                .orElseThrow();
        message.setChatId(chatId);
        repository.save(message);
        return message;
    }*/

   /* public List<ChatMessage> findChatMessages(String senderId, String recipientId){
        var chatId = chatRoomService.getChatRoomId(senderId,recipientId,false);
        return chatId.map(repository::findByChatId).orElse(new ArrayList<>());
    }*/

    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        Optional<String> chatIdOptional = chatRoomService.getChatRoomId(senderId, recipientId, true);

        if (chatIdOptional.isPresent()) {
            String chatId = chatIdOptional.get();

            List<ChatMessage> encryptedMessages = repository.findByChatId(chatId);

            // Şifreli mesajları çöz
            List<ChatMessage> decryptedMessages = new ArrayList<>();
            for (ChatMessage encryptedMessage : encryptedMessages) {
                String decryptedContent = null;
                try {
                    decryptedContent = MessageEncryption.decryptMessage(encryptedMessage.getContent(), chatId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                //ChatMessage decryptedMessage = new ChatMessage(encryptedMessage.getSenderId(), encryptedMessage.getRecipientId(), decryptedContent);
                ChatMessage decryptedMessage = new ChatMessage();
                decryptedMessage.setChatId(chatId);
                decryptedMessage.setContent(decryptedContent);
                decryptedMessage.setSenderId(encryptedMessage.getSenderId());
                decryptedMessage.setRecipientId(encryptedMessage.getRecipientId());
                decryptedMessages.add(decryptedMessage);
            }

            return decryptedMessages;
        } else {
            return new ArrayList<>();
        }
    }
}
