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
import com.alpha.ABCLogistics.Exception.OrderCannotBeCancelledException;
import com.alpha.ABCLogistics.Exception.OrderCannotBeLoadedException;
import com.alpha.ABCLogistics.Exception.OrderCannotBeUnloadedException;
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
	@Autowired
	MailService mailService;
	
	public ResponseEntity<ResponseStructure<Orders>> saveOrder(OrderDto dto) {
		Orders odd = new Orders();
		if(orderRepository.existsById(dto.getId())) {
			throw new OrderAlreadyExistsException("Order with id " + dto.getId() + " already exists");
		}			
		odd.setId(dto.getId());
		odd.setOrderdate(dto.getOrderdate());
		odd.setEmail(dto.getEmail());
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
		
		String subject = "Order Confirmation – Your Order Has Been Placed Successfully";

		String content = "Dear Customer,\n\n" + "Thank you for placing your order with ABC Logistics.\n\n"
				+ "Here are your order details:\n" + "------------------------------------\n" + "From: "
				+ saved.getLoading().getAddress().getCity() + "\n" + "To: "
				+ saved.getUnloading().getAddress().getCity() + "\n" + "Total Cost: ₹" + saved.getCost() + "\n"
				+ "Order Date: " + saved.getOrderdate() + "\n" + "Order ID: " + saved.getId() + "\n"
				+ "------------------------------------\n\n"
				+ "You will receive further updates once a carrier is assigned.\n\n"
				+ "Thank you for choosing ABC Logistics.\n\n" + "Best regards,\n" + "ABC Logistics Team";
		mailService.sendMail(saved.getEmail(), subject, content);
		
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
			ord = orderRepository.save(ord);
		}else {
			throw new TruckCapacityExceededException("Order weight "+totalwtoforder+" exceeds the available capacity of truck "+truckcapacity);
		}
		ResponseStructure<Orders> responseStructure = new ResponseStructure<Orders>();
		responseStructure.setData(ord);
		responseStructure.setMessage("Order updated successfully");
		responseStructure.setStatuscode(HttpStatus.ACCEPTED.value());
		
		String sub = "Carrier Assigned For Order";
		String content = "Dear Customer,\n\n"
			    + "Your order has been successfully assigned to the following carrier:\n\n"
			    + "Carrier Name: " + ord.getCarrier().getName() + "\n"
			    + "Contact Number: " + ord.getCarrier().getContact() + "\n"
			    + "Email: " + ord.getCarrier().getMail() + "\n\n"
			    + "You will be contacted soon for further updates.\n\n"
			    + "Thank you for choosing our service.";
		mailService.sendMail(ord.getEmail(), sub, content);
		
		return new ResponseEntity<ResponseStructure<Orders>>(responseStructure, HttpStatus.ACCEPTED);
	}
	public ResponseEntity<ResponseStructure<Orders>> updateOrderupdateLoading(int orderid, LoadingDto ldto) {
		Orders o = findOrder(orderid).getBody().getData();
		if(o.getStatus().equals("cancelled")) {
			throw new OrderCannotBeLoadedException("Order status is cancelled and cannot be loaded");
		}
		if(o.getCarrier() == null) {
			throw new OrderCannotBeLoadedException("Order carrier has not been assigned");
		}
		o.getLoading().setDate(ldto.getDate());
		o.getLoading().setTime(ldto.getTime());
		o.setStatus("transit"); 
		o = orderRepository.save(o);
		ResponseStructure<Orders> responseStructure = new ResponseStructure<Orders>();
		responseStructure.setData(o);
		responseStructure.setMessage("Order Loading updated successfully");
		responseStructure.setStatuscode(HttpStatus.ACCEPTED.value());
		String subject = "Order Loaded into the Truck";

		String content = "Dear Customer,\n\n"
		    + "We’re pleased to inform you that your order (Order ID: " + o.getId() + ") "
		    + "has been successfully loaded into the truck and is now ready for dispatch.\n\n"
		    + "Loading Details:\n"
		    + "------------------------------------\n"
		    + "Loading City: " + o.getLoading().getAddress().getCity() + "\n"
		    + "Date: " + o.getLoading().getDate() + "\n"
		    + "Time: " + o.getLoading().getTime() + "\n"
		    + "------------------------------------\n\n"
		    + "Your order is currently in the 'Transit' stage and will be on its way shortly.\n\n"
		    + "Thank you for choosing ABC Logistics.\n\n"
		    + "Best regards,\n"
		    + "ABC Logistics Team";
		
		mailService.sendMail(o.getEmail(), subject, content);
		return new ResponseEntity<ResponseStructure<Orders>>(responseStructure, HttpStatus.ACCEPTED);
	}
	public ResponseEntity<ResponseStructure<Orders>> updateOrderupdateUnloading(int orderid, LoadingDto ldto) {
		Orders o = findOrder(orderid).getBody().getData();
		if(o.getStatus().equals("cancelled")) {
			throw new OrderCannotBeUnloadedException("Order status is cancelled and cannot be unloaded");
		}
		if(!o.getStatus().equals("transit")) {
			throw new OrderCannotBeUnloadedException("Order has not been loaded and cannot be unloaded");
		}
		o.getUnloading().setDate(ldto.getDate());
		o.getUnloading().setTime(ldto.getTime());
		o.setStatus("delivered"); 
		Orders saved = orderRepository.save(o);
		ResponseStructure<Orders> responseStructure = new ResponseStructure<Orders>();
		responseStructure.setData(saved);
		responseStructure.setMessage("Order Unloading updated successfully");
		responseStructure.setStatuscode(HttpStatus.ACCEPTED.value());
		String subject ="Order Delivered Successfully";
		String content = "Dear Customer,\n\n"
			    + "We’re happy to inform you that your order (Order ID: " + saved.getId() + ") "
			    + "has been successfully delivered to its destination.\n\n"
			    + "Delivery Details:\n"
			    + "------------------------------------\n"
			    + "Destination City: " + saved.getUnloading().getAddress().getCity() + "\n"
			    + "Date: " + saved.getUnloading().getDate() + "\n"
			    + "Time: " + saved.getUnloading().getTime() + "\n"
			    + "------------------------------------\n\n"
			    + "Thank you for choosing ABC Logistics. We hope to serve you again soon.\n\n"
			    + "Best regards,\n"
			    + "ABC Logistics Team";
		mailService.sendMail(saved.getEmail(), subject, content);
		return new ResponseEntity<ResponseStructure<Orders>>(responseStructure, HttpStatus.ACCEPTED);
	}
	public ResponseEntity<ResponseStructure<Orders>> cancelOrder(int orderId) {
		Orders order = findOrder(orderId).getBody().getData();
		Orders saved = order;
		if(order.getStatus().equals("placed")) {
			order.setStatus("cancelled");
			saved = orderRepository.save(order);
		}else {
			throw new OrderCannotBeCancelledException("Order Cannot be cancelled because order status is "+order.getStatus());
		}
		ResponseStructure<Orders> responseStructure = new ResponseStructure<Orders>();
		responseStructure.setData(saved);
		responseStructure.setMessage("Order cancelled successfully");
		responseStructure.setStatuscode(HttpStatus.ACCEPTED.value());
		return new ResponseEntity<ResponseStructure<Orders>>(responseStructure, HttpStatus.ACCEPTED);
	}
}
