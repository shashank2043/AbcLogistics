package com.alpha.ABCLogistics.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alpha.ABCLogistics.DTO.OrderDto;
import com.alpha.ABCLogistics.Entity.Address;
import com.alpha.ABCLogistics.Entity.Cargo;
import com.alpha.ABCLogistics.Entity.Loading;
import com.alpha.ABCLogistics.Entity.Order;
import com.alpha.ABCLogistics.Entity.Unloading;
import com.alpha.ABCLogistics.Repository.AddressRepository;
import com.alpha.ABCLogistics.Repository.OrderRepository;

@Service
public class OrderService {
	@Autowired
	AddressRepository addressRepository;
	@Autowired
	OrderRepository orderRepository;

	public void saveOrder(OrderDto dto) {
		Order odd = new Order();
		odd.setId(dto.getId());
		odd.setOrderdate(dto.getOrderdate());
		int cost = 10*(dto.getCargoWt()*dto.getCargoCount());
		odd.setCost(cost);
		Cargo cargo = new Cargo(dto.getCargoId(), dto.getCargoName(), dto.getCargoDescription(), dto.getCargoWt(), dto.getCargoCount());
		odd.setCargo(cargo);
		Loading load = new Loading();
		Address loadAdd = addressRepository.findById(dto.getLoadingAddId()).get();
		load.setAddress(loadAdd);
		odd.setLoading(load);
		Unloading unload = new Unloading();
		Address unloadAdd = addressRepository.findById(dto.getUnloadingAddId()).get();
		unload.setAddress(unloadAdd);
		odd.setUnloading(unload);
		orderRepository.save(odd);
	}

}
