package dedeUnivers.dedeUnivers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import dedeUnivers.dedeUnivers.entities.UserAddress;

import java.util.Set;

public interface UserAddressRepository extends JpaRepository<UserAddress, Integer> {

    @Query("SELECT ua FROM UserAddress ua WHERE ua.user.id = :userId")
    Set<UserAddress> findByUserId(@Param("userId") int userId);

    @Query("SELECT ua FROM UserAddress ua WHERE ua.address.id = :addressId")
    Set<UserAddress> findByAddressId(@Param("addressId") Integer addressId);
}

