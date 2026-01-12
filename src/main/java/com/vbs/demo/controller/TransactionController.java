package com.vbs.demo.controller;

import com.vbs.demo.dto.TransactionDto;
import com.vbs.demo.dto.TransferDto;
import com.vbs.demo.models.Transaction;
import com.vbs.demo.models.User;
import com.vbs.demo.repositories.TransactionRepo;
import com.vbs.demo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class TransactionController {
    @Autowired
    UserRepo userRepo;

    @Autowired
    TransactionRepo transactionRepo;

    @PostMapping("/deposit")
    public String deposit(@RequestBody TransactionDto obj)
    {
        User user = userRepo.findById(obj.getId()).
                orElseThrow(()-> new RuntimeException("not found"));
        double newBalance = user.getBalance() + obj.getAmount();
        user.setBalance(newBalance);
        userRepo.save(user);


        Transaction t = new Transaction();
        t.setAmount(obj.getAmount());
        t.setCurrBalance(newBalance);
        t.setUserId(user.getId());
        t.setDescription("Rs. "+obj.getAmount()+" Deposit Successful");
        transactionRepo.save(t);

        return "Deposit Successful!";
    }


    @PostMapping("/withdraw")
    public String withdraw(@RequestBody TransactionDto obj)
    {
        User user = userRepo.findById(obj.getId()).
                orElseThrow(()-> new RuntimeException("not found"));
        double newBalance = user.getBalance() - obj.getAmount();
        if(newBalance<0)
        {
            return "Insufficient Balance";
        }
        user.setBalance(newBalance);
        userRepo.save(user);


        Transaction t = new Transaction();
        t.setAmount(obj.getAmount());
        t.setCurrBalance(newBalance);
        t.setUserId(user.getId());
        t.setDescription("Rs. "+obj.getAmount()+" Withdrawal Successful");
        transactionRepo.save(t);

        return "Withdrawal Successful!";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferDto obj)
    {
        User sender = userRepo.findById(obj.getId())
                .orElseThrow(()-> new RuntimeException("Not Found"));
        User rec = userRepo.findByUsername(obj.getUsername());

        if (rec==null) return "User not found";
        if(obj.getAmount()<1) return "Invalid Amount";
        if(sender.getId()== rec.getId()) return "Self-Transaction not allowed";

        double sbalance = sender.getBalance() - obj.getAmount() ;
        double rbalance = rec.getBalance() + obj.getAmount() ;

        if(sbalance<0) return "Insufficient Balance" ;

        sender.setBalance(sbalance);
        rec.setBalance(rbalance);

        userRepo.save(sender);
        userRepo.save(rec);

        Transaction ts = new Transaction();
        Transaction tr = new Transaction();

        ts.setAmount(obj.getAmount());
        ts.setCurrBalance(sbalance);
        ts.setUserId(sender.getId());
        ts.setDescription("Rs. "+obj.getAmount()+" Sent to user "+rec.getUsername());

        tr.setAmount(obj.getAmount());
        tr.setCurrBalance(rbalance);
        tr.setUserId(rec.getId());
        tr.setDescription("Rs. "+obj.getAmount()+" Recieved from user "+sender.getUsername());

        transactionRepo.save(ts);
        transactionRepo.save(tr);

        return "Transfer Done Successfully";
    }

    @GetMapping("/passbook/{id}")
    public List <Transaction> getPassbook(@PathVariable int id)
    {
        return transactionRepo.findAllByUserId(id);
    }

}
