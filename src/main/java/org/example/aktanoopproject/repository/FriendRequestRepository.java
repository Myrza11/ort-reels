package org.example.aktanoopproject.repository;

import org.example.aktanoopproject.model.FriendRequest;
import org.example.aktanoopproject.model.RequestStatus;
import org.example.aktanoopproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> getAllBySender(User currentUser);

    List<FriendRequest> getAllByRecipient(User currentUser);

    void deleteByRecipientId(Long friendId);

    @Query("SELECT fr FROM FriendRequest fr WHERE " +
            "(fr.sender.id = :id1 AND fr.recipient.id = :id2) OR " +
            "(fr.sender.id = :id2 AND fr.recipient.id = :id1)")
    Optional<FriendRequest> findByUsersInEitherOrder(@Param("id1") Long id1, @Param("id2") Long id2);

    List<FriendRequest> findBySenderOrRecipientAndStatus(User sender, User recipient, RequestStatus status);

    // ✅ 1. Проверка, являются ли пользователи друзьями (для чата)
    boolean existsBySenderAndRecipientAndStatus(User sender, User recipient, RequestStatus status);

    // ✅ 2. Найти всех друзей пользователя (принятые заявки)
    @Query("SELECT fr FROM FriendRequest fr WHERE " +
            "(fr.sender = :user OR fr.recipient = :user) AND fr.status = 'ACCEPTED'")
    List<FriendRequest> findAllAcceptedFriends(@Param("user") User user);

    // ✅ 3. Найти всех друзей (список пользователей)
    @Query("SELECT CASE WHEN fr.sender = :user THEN fr.recipient ELSE fr.sender END " +
            "FROM FriendRequest fr WHERE (fr.sender = :user OR fr.recipient = :user) AND fr.status = 'ACCEPTED'")
    List<User> findAllFriendsAsUsers(@Param("user") User user);
}
