package com.farmix.controller;

import com.farmix.entity.Extras;
import com.farmix.request.ExtrasRequest;
import com.farmix.response.MessageResponse;
import com.farmix.service.ExtrasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ExtrasController {

    @Autowired
    private ExtrasService extrasService;

    @PostMapping("/admin/extras/add")
    public ResponseEntity<Extras> addNewExtras(@RequestBody ExtrasRequest req) throws Exception {

        Extras extras = extrasService.addExtras(req.getName(), req.getRestaurantId());

        return new ResponseEntity<>(extras, HttpStatus.CREATED);
    }

    @PutMapping("/admin/extras/{id}/stock")
    public ResponseEntity<Extras> updateInStock(@PathVariable Long id){
        Extras extras = extrasService.updateInStock(id);
        return new ResponseEntity<>(extras, HttpStatus.OK);
    }

    @DeleteMapping("/admin/extras/{id}")
    public ResponseEntity<MessageResponse> deleteExtras(@PathVariable Long id) throws Exception {

        extrasService.deleteExtras(id);

        MessageResponse msg = new MessageResponse();
        msg.setMessage("Successfully deleted extra");

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @GetMapping("/{id}/extras")
    public ResponseEntity<List<Extras>> getExtras(@PathVariable Long id) throws Exception {
        List<Extras> extrasList = extrasService.findExtrasByRestaurantId(id);

        return new ResponseEntity<>(extrasList, HttpStatus.OK);
    }

}
