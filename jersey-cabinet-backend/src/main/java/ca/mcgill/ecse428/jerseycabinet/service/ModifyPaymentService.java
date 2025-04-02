package ca.mcgill.ecse428.jerseycabinet.service;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ca.mcgill.ecse428.jerseycabinet.dao.PaymentMethodRepository;
import ca.mcgill.ecse428.jerseycabinet.model.PaymentMethod;
import jakarta.transaction.Transactional;

@Service
public class ModifyPaymentService {
    @Autowired
    private PaymentMethodRepository PaymentMethodRepo;
    @Transactional
    public void deleteCardById(int id) throws Exception{
        PaymentMethod j = PaymentMethodRepo.findPaymentMethodById(id);
        if (j == null){
            throw new Exception("There is no PaymentMethod with ID" + id);
        }else{
            PaymentMethodRepo.delete(j);
        }
    }

    @Transactional
    public PaymentMethod modifyAddressById(int id, String desc) throws Exception{
        PaymentMethod j = PaymentMethodRepo.findPaymentMethodById(id);
        if (j == null){
            throw new Exception("There is no PaymentMethod with ID" + id);
        }
        if(desc==""){
            throw new Exception("Card information is invalid");
        }else{
            j.setBillingAddress(desc);
        }   
        return PaymentMethodRepo.save(j);
    }

    @Transactional
    public PaymentMethod modifyHolderById(int id, String desc) throws Exception{
        PaymentMethod j = PaymentMethodRepo.findPaymentMethodById(id);
        if (j == null){
            throw new Exception("There is no PaymentMethod with ID" + id);
        }
        if(desc==""){
            throw new Exception("Card information is invalid");
        }else{
        j.setCardName(desc);}
        return PaymentMethodRepo.save(j);
    }

    @Transactional
    public PaymentMethod modifyNumberById(int id, String desc) throws Exception{
        PaymentMethod j = PaymentMethodRepo.findPaymentMethodById(id);
        if (j == null){
            throw new Exception("There is no PaymentMethod with ID" + id);
        }if(desc==""){
            throw new Exception("Card information is invalid");
        }else{
        j.setCardNumber(desc);}
        return PaymentMethodRepo.save(j);
    }

    @Transactional
    public PaymentMethod modifyCVVById(int id, String desc) throws Exception{
        PaymentMethod j = PaymentMethodRepo.findPaymentMethodById(id);
        if (j == null){
            throw new Exception("There is no PaymentMethod with ID" + id);
        }if(desc==""){
            throw new Exception("Card information is invalid");
        }else{
        j.setCardCVV(desc);}
        return PaymentMethodRepo.save(j);
    }

    @Transactional
    public PaymentMethod modifyExpiryById(int id, String desc) throws Exception{
        PaymentMethod j = PaymentMethodRepo.findPaymentMethodById(id);
        if (j == null){
            throw new Exception("There is no PaymentMethod with ID" + id);
        }if(desc==""){
            throw new Exception("Card information is invalid");
        }else{
        Date date = Date.valueOf(desc);    
        j.setCardExpiryDate(date);}
        return PaymentMethodRepo.save(j);
    }
}
