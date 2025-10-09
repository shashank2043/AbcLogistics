package com.alpha.ABCLogistics.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alpha.ABCLogistics.DTO.LoadingDto;
import com.alpha.ABCLogistics.DTO.OrderDto;
import com.alpha.ABCLogistics.DTO.ResponseStructure;
import com.alpha.ABCLogistics.Entity.Address;
import com.alpha.ABCLogistics.Entity.Cargo;
import com.alpha.ABCLogistics.Entity.Loading;
import com.alpha.ABCLogistics.Entity.Orders;
import com.alpha.ABCLogistics.Entity.Truck;
import com.alpha.ABCLogistics.Entity.Unloading;
import com.alpha.ABCLogistics.Exception.AddressNotFoundException;
import com.alpha.ABCLogistics.Exception.CargoAlreadyExistsException;
import com.alpha.ABCLogistics.Exception.OrderAlreadyExistsException;
import com.alpha.ABCLogistics.Exception.OrderNotFoundException;
import com.alpha.ABCLogistics.Exception.TruckCapacityExceededException;
import com.alpha.ABCLogistics.Exception.TruckNotFoundException;
import com.alpha.ABCLogistics.Repository.AddressRepository;
import com.alpha.ABCLogistics.Repository.CargoRepository;
import com.alpha.ABCLogistics.Repository.OrderRepository;
import com.alpha.ABCLogistics.Repository.TruckRepository;

@Service
public class OrderService {
	@Autowired
	AddressRepository addressRepository;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	CargoRepository cargoRepository;
	@Autowired
	TruckRepository truckRepository;
	public ResponseEntity<ResponseStructure<Orders>> saveOrder(OrderDto dto) {
		Orders odd = new Orders();
		if(orderRepository.existsById(dto.getId())) {
			throw new OrderAlreadyExistsException("Order with id " + dto.getId() + " already exists");
		}			
		odd.setId(dto.getId());
		odd.setOrderdate(dto.getOrderdate());
		int cost = 10*(dto.getCargoWt()*dto.getCargoCount());
		odd.setCost(cost);
		if(cargoRepository.existsById(dto.getCargoId())) {
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
		
		Orders saved = orderRepository.save(odd);
		ResponseStructure<Orders> responseStructure = new ResponseStructure<Orders>();
		
		responseStructure.setData(saved);
		responseStructure.setMessage("Order saved successfully");
		responseStructure.setStatuscode(HttpStatus.CREATED.value());
		return new ResponseEntity<ResponseStructure<Orders>>(responseStructure, HttpStatus.CREATED);
	}
	public ResponseEntity<ResponseStructure<Orders>> findOrder(int id) {
		Orders order = orderRepository.findById(id).orElseThrow(()->new OrderNotFoundException("Order with id " + id + " not found"));
		ResponseStructure<Orders> responseStructure = new ResponseStructure<Orders>();
		responseStructure.setData(order);
		responseStructure.setMessage("Order found successfully");
		responseStructure.setStatuscode(HttpStatus.FOUND.value());
		return new ResponseEntity<ResponseStructure<Orders>>(responseStructure, HttpStatus.FOUND);
	}
	public ResponseEntity<ResponseStructure<Orders>> deleteOrder(int id) {
		Orders order = orderRepository.findById(id).orElseThrow(()->new OrderNotFoundException("Order with id " + id + " not found"));
		orderRepository.delete(order);
		ResponseStructure<Orders> responseStructure = new ResponseStructure<Orders>();
		responseStructure.setData(order);
		responseStructure.setMessage("Order deleted successfully");
		responseStructure.setStatuscode(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure<Orders>>(responseStructure, HttpStatus.OK);
	}
	public ResponseEntity<ResponseStructure<Orders>> updateOrder(int orderid, int truckid) {
		Orders ord = orderRepository.findById(orderid).orElseThrow(()->new OrderNotFoundException("Order with id " + orderid + " not found"));
		Truck truck = truckRepository.findById(truckid).orElseThrow(()->new TruckNotFoundException("Truck with id " + truckid + " not found"));
		int totalwtoforder = (ord.getCargo().getWeight()*ord.getCargo().getCount());
		int truckcapacity = truck.getCapacity();
		if(truckcapacity>=totalwtoforder) {
			ord.setCarrier(truck.getCarrier());
			truck.setCapacity(truck.getCapacity()-totalwtoforder);
			truckRepository.save(truck);
			orderRepository.save(ord);
			
		}else {
			throw new TruckCapacityExceededException("Order weight "+totalwtoforder+" exceeds the available capacity of truck "+truckcapacity);
		}
		ResponseStructure<Orders> responseStructure = new ResponseStructure<Orders>();
		responseStructure.setData(ord);
		responseStructure.setMessage("Order updated successfully");
		responseStructure.setStatuscode(HttpStatus.ACCEPTED.value());
		return new ResponseEntity<ResponseStructure<Orders>>(responseStructure, HttpStatus.ACCEPTED);
	}
	public ResponseEntity<ResponseStructure<Orders>> updateOrderupdateLoading(int orderid, LoadingDto ldto) {
		Orders o = findOrder(orderid).getBody().getData();
		o.getLoading().setDate(ldto.getDate());
		o.getLoading().setTime(ldto.getTime());
		o.setStatus("pending"); 
		Orders saved = orderRepository.save(o);
		ResponseStructure<Orders> responseStructure = new ResponseStructure<Orders>();
		responseStructure.setData(saved);
		responseStructure.setMessage("Order updated successfully");
		responseStructure.setStatuscode(HttpStatus.ACCEPTED.value());
		return new ResponseEntity<ResponseStructure<Orders>>(responseStructure, HttpStatus.ACCEPTED);
	}
}
