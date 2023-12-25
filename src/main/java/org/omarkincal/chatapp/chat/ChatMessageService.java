package org.omarkincal.chatapp.chat;

import lombok.RequiredArgsConstructor;
import org.omarkincal.chatapp.chatroom.ChatRoomService;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService{
    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;

  /*  SecretKey secretKey;

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
    public ChatMessage saveMessage(ChatMessage message){
        var chatId = chatRoomService.getChatRoomId(
                message.getSenderId(),
                message.getRecipientId(),
                true)
                .orElseThrow();
        message.setChatId(chatId);
        repository.save(message);
        return message;
    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId){
        var chatId = chatRoomService.getChatRoomId(senderId,recipientId,false);
        return chatId.map(repository::findByChatId).orElse(new ArrayList<>());
    }


}
