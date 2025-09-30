package com.alpha.ABCLogistics.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alpha.ABCLogistics.DTO.OrderDto;
import com.alpha.ABCLogistics.DTO.ResponseStructure;
import com.alpha.ABCLogistics.Entity.Address;
import com.alpha.ABCLogistics.Entity.Cargo;
import com.alpha.ABCLogistics.Entity.Loading;
import com.alpha.ABCLogistics.Entity.Order;
import com.alpha.ABCLogistics.Entity.Unloading;
import com.alpha.ABCLogistics.Exception.AddressNotFoundException;
import com.alpha.ABCLogistics.Exception.CargoAlreadyExistsException;
import com.alpha.ABCLogistics.Exception.OrderAlreadyExistsException;
import com.alpha.ABCLogistics.Exception.OrderNotFoundException;
import com.alpha.ABCLogistics.Repository.AddressRepository;
import com.alpha.ABCLogistics.Repository.CargoService;
import com.alpha.ABCLogistics.Repository.OrderRepository;

@Service
public class OrderService {
	@Autowired
	AddressRepository addressRepository;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	CargoService cargoService;
	public ResponseEntity<ResponseStructure<Order>> saveOrder(OrderDto dto) {
		Order odd = new Order();
		if(orderRepository.existsById(dto.getId())) {
			throw new OrderAlreadyExistsException("Order with id " + dto.getId() + " already exists");
		}			
		odd.setId(dto.getId());
		odd.setOrderdate(dto.getOrderdate());
		int cost = 10*(dto.getCargoWt()*dto.getCargoCount());
		odd.setCost(cost);
		if(cargoService.existsById(dto.getCargoId())) {
			throw new CargoAlreadyExistsException("Cargo with id " + dto.getCargoId() + " already exists");
		}
		Cargo cargo = new Cargo(dto.getCargoId(), dto.getCargoName(), dto.getCargoDescription(), dto.getCargoWt(), dto.getCargoCount());
		odd.setCargo(cargo);
		Loading load = new Loading();
		Address loadAdd = addressRepository.findById(dto.getLoadingAddId()).orElseThrow(()->new AddressNotFoundException("Address with id " + dto.getLoadingAddId() + " not found"));
		load.setAddress(loadAdd);
		odd.setLoading(load);
		Unloading unload = new Unloading();
		Address unloadAdd = addressRepository.findById(dto.getUnloadingAddId()).orElseThrow(()->new AddressNotFoundException("Address with id " + dto.getUnloadingAddId() + " not found"));
		unload.setAddress(unloadAdd);
		odd.setUnloading(unload);
		orderRepository.save(odd);
		ResponseStructure<Order> responseStructure = new ResponseStructure<Order>();
		responseStructure.setData(odd);
		responseStructure.setMessage("Order saved successfully");
		responseStructure.setStatuscode(HttpStatus.CREATED.value());
		return new ResponseEntity<ResponseStructure<Order>>(responseStructure, HttpStatus.CREATED);
	}
	public ResponseEntity<ResponseStructure<Order>> findOrder(int id) {
		Order order = orderRepository.findById(id).orElseThrow(()->new OrderNotFoundException("Order with id " + id + " not found"));
		ResponseStructure<Order> responseStructure = new ResponseStructure<Order>();
		responseStructure.setData(order);
		responseStructure.setMessage("Order found successfully");
		responseStructure.setStatuscode(HttpStatus.FOUND.value());
		return new ResponseEntity<ResponseStructure<Order>>(responseStructure, HttpStatus.FOUND);
	}
	public ResponseEntity<ResponseStructure<Order>> deleteOrder(int id) {
		Order order = orderRepository.findById(id).orElseThrow(()->new OrderNotFoundException("Order with id " + id + " not found"));
		orderRepository.delete(order);
		ResponseStructure<Order> responseStructure = new ResponseStructure<Order>();
		responseStructure.setData(order);
		responseStructure.setMessage("Order deleted successfully");
		responseStructure.setStatuscode(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure<Order>>(responseStructure, HttpStatus.OK);
	}
	

}
