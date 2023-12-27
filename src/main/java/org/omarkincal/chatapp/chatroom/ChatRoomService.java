package org.omarkincal.chatapp.chatroom;

import lombok.RequiredArgsConstructor;
import org.omarkincal.chatapp.chat.MessageEncryption;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository repository;

    public Optional<String> getChatRoomId(String senderId, String recipientId, Boolean isRoomExist) {
        return repository.findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatKey)
                .or(() -> {
                            if (isRoomExist) {
                                var chatId = createChat(senderId, recipientId);
                                return Optional.of(chatId);
                            }
                            return Optional.empty();
                        }

                );
    }

    private String createChat(String senderId, String recipientId) {
        SecretKey chatKey = MessageEncryption.generateChatRoomKey(senderId,recipientId);
        String chatId = MessageEncryption.secretKeyToString(chatKey);
        ChatRoom senderRecipient = ChatRoom.builder()
                .chatKey(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        ChatRoom recipientSender = ChatRoom.builder()
                .chatKey(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();

        repository.save(senderRecipient);
        repository.save(recipientSender);

        return chatId;
    }

}
