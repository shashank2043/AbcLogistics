package com.alpha.ABCLogistics.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alpha.ABCLogistics.DTO.ResponseStructure;
import com.alpha.ABCLogistics.Entity.Driver;
import com.alpha.ABCLogistics.Exception.DriverAlreadyPresentException;
import com.alpha.ABCLogistics.Exception.DriverNotFoundException;
import com.alpha.ABCLogistics.Repository.DriverRepository;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    public ResponseEntity<ResponseStructure<Driver>> saveDriver(Driver driver) {
        Optional<Driver> driverOpt = driverRepository.findById(driver.getId());
        if (driverOpt.isPresent()) {
            throw new DriverAlreadyPresentException();
        }
        Driver savedDriver = driverRepository.save(driver);

        ResponseStructure<Driver> responseStructure = new ResponseStructure<>();
        responseStructure.setData(savedDriver);
        responseStructure.setMessage("Driver Saved");
        responseStructure.setStatuscode(HttpStatus.CREATED.value());

        return new ResponseEntity<>(responseStructure, HttpStatus.CREATED);
    }

    public ResponseEntity<ResponseStructure<Driver>> findDriver(int id) {
        Optional<Driver> driverOpt = driverRepository.findById(id);
        if (driverOpt.isEmpty()) {
            throw new DriverNotFoundException();
        }
        Driver driver = driverOpt.get();

        ResponseStructure<Driver> responseStructure = new ResponseStructure<>();
        responseStructure.setData(driver);
        responseStructure.setMessage("Driver Found");
        responseStructure.setStatuscode(HttpStatus.FOUND.value());

        return new ResponseEntity<>(responseStructure, HttpStatus.FOUND);
    }

    public ResponseEntity<ResponseStructure<Driver>> deleteDriver(int id) {
        Optional<Driver> driverOpt = driverRepository.findById(id);
        if (driverOpt.isEmpty()) {
            throw new DriverNotFoundException();
        }
        Driver driver = driverOpt.get();
        driverRepository.delete(driver);

        ResponseStructure<Driver> responseStructure = new ResponseStructure<>();
        responseStructure.setData(driver);
        responseStructure.setMessage("Driver Deleted");
        responseStructure.setStatuscode(HttpStatus.ACCEPTED.value());

        return new ResponseEntity<>(responseStructure, HttpStatus.ACCEPTED);
    }
}
