package com.alpha.ABCLogistics.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.ABCLogistics.DTO.OrderDto;
import com.alpha.ABCLogistics.DTO.ResponseStructure;
import com.alpha.ABCLogistics.DTO.TruckDto;
import com.alpha.ABCLogistics.Entity.Address;
import com.alpha.ABCLogistics.Entity.Carrier;
import com.alpha.ABCLogistics.Entity.Driver;
import com.alpha.ABCLogistics.Entity.Order;
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
	@PostMapping("/savedriver")
	public ResponseEntity<ResponseStructure<Driver>> saveDriver(@RequestBody Driver driver) {
		return driverService.saveDriver(driver);
	}
	@GetMapping("/finddriver")
	public ResponseEntity<ResponseStructure<Driver>> findDriver(@RequestParam int id) {
		return driverService.findDriver(id);
	}
	@DeleteMapping("/deletedriver")
	public ResponseEntity<ResponseStructure<Driver>> deleteDriver(@RequestParam int id) {
		return driverService.deleteDriver(id);
	}
	@PostMapping("/saveorder")
	public ResponseEntity<ResponseStructure<Order>> saveOrder(@RequestBody @Valid OrderDto orderDto) {
		return orderService.saveOrder(orderDto);
	}
	@GetMapping("/findorder")
	public ResponseEntity<ResponseStructure<Order>> findOrder(@RequestParam int id) {
		return orderService.findOrder(id);
	}
	@DeleteMapping("/deleteorder")
	public ResponseEntity<ResponseStructure<Order>> deleteOrder(@RequestParam int id) {
		return orderService.deleteOrder(id);
	}
}
