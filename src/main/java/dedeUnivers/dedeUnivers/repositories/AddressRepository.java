package dedeUnivers.dedeUnivers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import dedeUnivers.dedeUnivers.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}

