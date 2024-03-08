package net.iqbalfauzan.belajarspringbootrestfulapi.controller;

import net.iqbalfauzan.belajarspringbootrestfulapi.entity.User;
import net.iqbalfauzan.belajarspringbootrestfulapi.model.request.CreateAddressRequest;
import net.iqbalfauzan.belajarspringbootrestfulapi.model.response.AddressResponse;
import net.iqbalfauzan.belajarspringbootrestfulapi.model.response.WebResponse;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddressController {

    public WebResponse<AddressResponse> create(User user, CreateAddressRequest request, String contactId){
        
    }

}
