package javawizzards.officespace.repository;

import javawizzards.officespace.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findById(UUID id);
    Optional<Payment> findByCustomerEmail(String email);
    Optional<Payment> findByChargeId(String cardNumber);
}
