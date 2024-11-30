package dedeUnivers.dedeUnivers.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dedeUnivers.dedeUnivers.entities.Address;
import dedeUnivers.dedeUnivers.entities.User;
import dedeUnivers.dedeUnivers.entities.UserAddress;
import dedeUnivers.dedeUnivers.repositories.AddressRepository;
import dedeUnivers.dedeUnivers.repositories.UserAddressRepository;
import dedeUnivers.dedeUnivers.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private UserRepository userRepository;

    public Address save(Address adress) {
        return addressRepository.save(adress);
    }

    public Address findById(Integer id) {
        return addressRepository.findById(id).get();
    }

    public List<Address> getAllAdress() {
        return addressRepository.findAll();
    }

    public Address addAddress(Address address, Integer userId){
        User user = userRepository.findById(userId).get();
        Address address1 = addressRepository.save(address);
        UserAddress userAddress = new UserAddress();
        userAddress.setAddress(address1);
        userAddress.setUser(user);
        userAddressRepository.save(userAddress);

        return address1;
    }

    public Set<Address> getAllAddress(Integer userId) {
        Set<UserAddress> userAddresses = userAddressRepository.findByUserId(userId);
        return userAddresses.stream()
                .map(UserAddress::getAddress)
                .collect(Collectors.toSet());
    }

    @Transactional
    public void deleteAddressById(Integer addressId) {
        // Récupérer les UserAddress associés à l'adresse
        Set<UserAddress> userAddresses = userAddressRepository.findByAddressId(addressId);

        // Supprimer chaque UserAddress de la base de données
        userAddressRepository.deleteAll(userAddresses);

        // Supprimer l'adresse elle-même
        addressRepository.deleteById(addressId);
    }
}

