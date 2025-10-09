package com.alpha.ABCLogistics.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.ABCLogistics.DTO.DriverDto;
import com.alpha.ABCLogistics.DTO.LoadingDto;
import com.alpha.ABCLogistics.DTO.ResponseStructure;
import com.alpha.ABCLogistics.DTO.TruckDto;
import com.alpha.ABCLogistics.Entity.Address;
import com.alpha.ABCLogistics.Entity.Carrier;
import com.alpha.ABCLogistics.Entity.Driver;
import com.alpha.ABCLogistics.Entity.Orders;
import com.alpha.ABCLogistics.Entity.Truck;
import com.alpha.ABCLogistics.Service.AddressService;
import com.alpha.ABCLogistics.Service.CarrierService;
import com.alpha.ABCLogistics.Service.DriverService;
import com.alpha.ABCLogistics.Service.TruckService;
import com.alpha.ABCLogistics.Service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	AddressService addressService;
	@Autowired
	CarrierService carrierService;
	@Autowired
	TruckService truckService;
	@Autowired
	DriverService driverService;
	@Autowired
	OrderService orderService;
	
	//Address Routes
	@PostMapping("/saveaddress")
	public ResponseEntity<ResponseStructure<Address>> saveAddress(@RequestBody @Valid Address address)
	{
		return addressService.saveAddress(address);
	}
	@GetMapping("/findaddress")
	public ResponseEntity<ResponseStructure<Address>> findAddress(@RequestParam int id) {
		return addressService.findAddress(id);
	}
	@DeleteMapping("/deleteaddress")
	public ResponseEntity<ResponseStructure<Address>> deleteAddress(@RequestParam int id) {
		return addressService.deleteAddress(id);
	}
	//Carrier Routes
	@PostMapping("/savecarrier")
	public ResponseEntity<ResponseStructure<Carrier>> saveCarrier(@RequestBody Carrier carrier) {
		return carrierService.saveCarrier(carrier);
	}
	@GetMapping("/findcarrier")
	public ResponseEntity<ResponseStructure<Carrier>> findCarrier(@RequestParam int id) {
		return carrierService.findCarrier(id);
	}
	@DeleteMapping("/deletecarrier")
	public ResponseEntity<ResponseStructure<Carrier>> deleteCarrier(@RequestParam int id) {
		return carrierService.deleteCarrier(id);
	}
	//Truck Routes
	@PostMapping("/savetruck")
	public ResponseEntity<ResponseStructure<Truck>> saveTruck(@RequestBody @Valid TruckDto truckDto) {
		return truckService.saveTruck(truckDto);
	}
	@GetMapping("/findtruck")
	public ResponseEntity<ResponseStructure<Truck>> findTruck(@RequestParam int id) {
		return truckService.findTruck(id);
	}
	@DeleteMapping("/deletetruck")
	public ResponseEntity<ResponseStructure<Truck>> deleteTruck(@RequestParam int id) {
		return truckService.deleteTruck(id);
	}
	@PutMapping("/updatetruck/{truckid}/assignCarrier/{carrierid}")
	public ResponseEntity<ResponseStructure<Truck>> updateTruck(@PathVariable int truckid,@PathVariable int carrierid) {
		return truckService.updateTruck(truckid,carrierid);
	}
	//Driver Routes
	@PostMapping("/savedriver")
	public ResponseEntity<ResponseStructure<Driver>> saveDriver(@RequestBody DriverDto driverdto) {
		return driverService.saveDriver(driverdto);
	}
	@GetMapping("/finddriver")
	public ResponseEntity<ResponseStructure<Driver>> findDriver(@RequestParam int id) {
		return driverService.findDriver(id);
	}
	@DeleteMapping("/deletedriver")
	public ResponseEntity<ResponseStructure<Driver>> deleteDriver(@RequestParam int id) {
		return driverService.deleteDriver(id);
	}
	@PutMapping("/updatedriver/{driverid}/assignCarrier/{carrierid}/assigntruck/{truckid}")
	public ResponseEntity<ResponseStructure<Driver>> updateDriver(@PathVariable int driverid,@PathVariable int carrierid,@PathVariable int truckid) {
		return driverService.updateDriver(driverid,carrierid,truckid);
	}
	//Order Routes
	@GetMapping("/findorder")
	public ResponseEntity<ResponseStructure<Orders>> findOrder(@RequestParam int id) {
		return orderService.findOrder(id);
	}
	@DeleteMapping("/deleteorder")
	public ResponseEntity<ResponseStructure<Orders>> deleteOrder(@RequestParam int id) {
		return orderService.deleteOrder(id);
	}
	@PutMapping("/updateorder/{orderid}/assignCarrier/{truckid}")
	public ResponseEntity<ResponseStructure<Orders>> updateorder(@PathVariable int orderid,@PathVariable int truckid) {
		return orderService.updateOrder(orderid,truckid);
	}
	@PutMapping("/updateorder/{orderid}/updateLoading")
	public ResponseEntity<ResponseStructure<Orders>> updateOrderupdateLoading(@PathVariable int orderid,@RequestBody LoadingDto ldto) {
		return orderService.updateOrderupdateLoading(orderid,ldto);
	}
}
