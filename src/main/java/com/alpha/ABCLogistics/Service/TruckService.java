package com.alpha.ABCLogistics.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alpha.ABCLogistics.DTO.ResponseStructure;
import com.alpha.ABCLogistics.DTO.TruckDto;
import com.alpha.ABCLogistics.Entity.Carrier;
import com.alpha.ABCLogistics.Entity.Truck;
import com.alpha.ABCLogistics.Exception.CarrierNotFoundException;
import com.alpha.ABCLogistics.Exception.TruckAlreadyPresentException;
import com.alpha.ABCLogistics.Exception.TruckNotFoundException;
import com.alpha.ABCLogistics.Repository.CarrierRepository;
import com.alpha.ABCLogistics.Repository.TruckRepository;

@Service
public class TruckService {
	@Autowired
	TruckRepository truckRepository;
	@Autowired
	private CarrierRepository carrierRepository;

	public ResponseEntity<ResponseStructure<Truck>> saveTruck(TruckDto truckDto) {
		Optional<Truck> truckOptional = truckRepository.findById(truckDto.getId());
		if(truckOptional.isPresent()) {
			throw new TruckAlreadyPresentException();
		}
		Truck truck = new Truck();
		truck.setId(truckDto.getId());
		truck.setName(truckDto.getName());
		truck.setCapacity(truckDto.getCapacity());
		truck.setNumber(truckDto.getNumber());
		truck.setStatus(truckDto.getStatus());
		truckRepository.save(truck);
		ResponseStructure<Truck> responseStructure = new ResponseStructure<Truck>();
		responseStructure.setData(truck);
		responseStructure.setMessage("Truck Saved");;
		responseStructure.setStatuscode(HttpStatus.ACCEPTED.value());
		return new ResponseEntity<ResponseStructure<Truck>>(responseStructure, HttpStatus.ACCEPTED);
	}

	public ResponseEntity<ResponseStructure<Truck>> findTruck(int id) {
		Optional<Truck> truckOptional = truckRepository.findById(id);
		if(truckOptional.isEmpty()) {
			throw new TruckNotFoundException();
		}
		Truck truck = truckOptional.get();
		ResponseStructure<Truck> responseStructure = new ResponseStructure<Truck>();
		responseStructure.setData(truck);
		responseStructure.setMessage("Truck Found");;
		responseStructure.setStatuscode(HttpStatus.FOUND.value());
		return new ResponseEntity<ResponseStructure<Truck>>(responseStructure, HttpStatus.FOUND);
	}

	public ResponseEntity<ResponseStructure<Truck>> deleteTruck(int id) {
		Optional<Truck> truckOptional = truckRepository.findById(id);
		if(truckOptional.isEmpty()) {
			throw new TruckNotFoundException();
		}
		Truck truck = truckOptional.get();
		truckRepository.delete(truck);
		ResponseStructure<Truck> responseStructure = new ResponseStructure<Truck>();
		responseStructure.setData(truck);
		responseStructure.setMessage("Truck Deleted");;
		responseStructure.setStatuscode(HttpStatus.ACCEPTED.value());
		return new ResponseEntity<ResponseStructure<Truck>>(responseStructure, HttpStatus.ACCEPTED);
	}

	public ResponseEntity<ResponseStructure<Truck>> updateTruck(int truckid, int carrierid) {
		Truck truck = truckRepository.findById(truckid).orElseThrow(()->new TruckNotFoundException("Truck with id " + truckid + " not found"));
		Carrier carrier = carrierRepository.findById(carrierid).orElseThrow(()->new CarrierNotFoundException("Carrier with id " + carrierid + " not found"));
		truck.setCarrier(carrier);
		truckRepository.save(truck);
		ResponseStructure<Truck> responseStructure = new ResponseStructure<Truck>();
		responseStructure.setData(truck);
		responseStructure.setMessage("Truck Updated");;
		responseStructure.setStatuscode(HttpStatus.ACCEPTED.value());
		return new ResponseEntity<ResponseStructure<Truck>>(responseStructure, HttpStatus.ACCEPTED);
	}
	

}
