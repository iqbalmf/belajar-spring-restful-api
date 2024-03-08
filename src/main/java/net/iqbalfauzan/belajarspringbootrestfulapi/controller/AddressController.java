package net.iqbalfauzan.belajarspringbootrestfulapi.controller;

import net.iqbalfauzan.belajarspringbootrestfulapi.entity.User;
import net.iqbalfauzan.belajarspringbootrestfulapi.model.request.CreateAddressRequest;
import net.iqbalfauzan.belajarspringbootrestfulapi.model.request.UpdateAddressRequest;
import net.iqbalfauzan.belajarspringbootrestfulapi.model.response.AddressResponse;
import net.iqbalfauzan.belajarspringbootrestfulapi.model.response.WebResponse;
import net.iqbalfauzan.belajarspringbootrestfulapi.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.util.List;

@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping(
            path = "/api/contacts/{contactId}/addresses",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> create(User user,
                                               @RequestBody CreateAddressRequest request,
                                               @PathVariable("contactId") String contactId) {

        request.setContactId(contactId);
        AddressResponse addressResponse = addressService.create(user, request);
        return WebResponse.<AddressResponse>builder().data(addressResponse).build();

    }

    @GetMapping(
            path = "/api/contacts/{contactId}/addresses/{addressId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> get(User user,
                                            @PathVariable("contactId") String contactId,
                                            @PathVariable("addressId") String addressId) {
        AddressResponse addressResponse = addressService.get(user, contactId, addressId);
        return WebResponse.<AddressResponse>builder().data(addressResponse).build();

    }

    @PutMapping(
            path = "/api/contacts/{contactId}/addresses/{addressId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> update(User user,
                                               @RequestBody UpdateAddressRequest request,
                                               @PathVariable("contactId") String contactId,
                                               @PathVariable("addressId") String addressId) {

        request.setContactId(contactId);
        request.setAddressId(addressId);
        AddressResponse addressResponse = addressService.update(user, request);
        return WebResponse.<AddressResponse>builder().data(addressResponse).build();

    }

    @DeleteMapping(
            path = "/api/contacts/{contactId}/addresses/{addressId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(User user,
                                      @PathVariable("contactId") String contactId,
                                      @PathVariable("addressId") String addressId) {
        addressService.delete(user, contactId, addressId);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
            path = "/api/contacts/{contactId}/addresses",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<AddressResponse>> getList(User user,
                                                      @PathVariable("contactId") String contactId){
        List<AddressResponse> addressResponses = addressService.getList(user, contactId);
        return WebResponse.<List<AddressResponse>>builder().data(addressResponses).build();
    }

}
