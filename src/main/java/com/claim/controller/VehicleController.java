package com.claim.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.claim.service.VehicleService;
import com.claim.entity.Vehicle;

@Controller
public class VehicleController {

	// dependency injection
	@Autowired
	VehicleService vehicleService;

	@GetMapping("/")
	public String welcome(Model model) {
		return "home";
	}

	@GetMapping("/home")
	public String loadHome(Model model) {
		return "home";
	}

	@GetMapping("/inventory")
	public String handleDisplayAll(Model model) {
		List<Vehicle> vehicles = vehicleService.getVehicles();
		model.addAttribute("vehicles", vehicles);
		return "inventory";
	}

	// TODO display vehicle details

	// TODO add a new vehicle to the inventory @GetMapping
	@GetMapping("/addVehicle")
	public ModelAndView addNewVehicle(Model model) {
		System.out.println("getmapping add new vehicle fired");
		return new ModelAndView("addVehicle", "vehicle", new Vehicle());
	}

	// TODO add a new vehicle to the inventory @PostMapping
	@PostMapping("/addVehicle")
	public String handleAddNewVehicle(Model model, @ModelAttribute("vehicle") Vehicle vehicle, HttpSession session) {
		System.out.println("postmapping add new vehicle fired");
		model.addAttribute("newVehicle", vehicle);
		vehicleService.saveVehicle(vehicle);
		// possibly redirect to a inventoryUpdated.jsp or form of thank-you/notification
		// or add a label to show add confirmation
		return "addVehicle";
	}

	// TODO if vehicle in inventory >120 days, user can bid and at max 10% discount
	@GetMapping("/bid")
	public String handleDisplayAllIdle(Model model) throws ParseException {
		List<Vehicle> allVehicles = vehicleService.getVehicles();
		List<Vehicle> idleVehicles = new ArrayList<Vehicle>();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		Date d = sdformat.parse("2021-12-30");
		double discountedPrice = 0;
		// date comparison
		for (Vehicle temp : allVehicles) {
			if (temp.getDopDealer().before(d)) {
				//System.out.println(temp);
				// modify price
				discountedPrice = temp.getPrice() * 0.9;
				// set discounted price and add to idleVehicle list
				temp.setPrice(discountedPrice);
				idleVehicles.add(temp);
			}
		}
		model.addAttribute("vehicles", idleVehicles);
		return "bid";
	}

	// TODO search for a type "model" of vehicle from the inventory
	@GetMapping("/searchByModel")
	public ModelAndView searchByModel(Model model) {
		System.out.println("getmapping searchByModel fired");
		return new ModelAndView("searchByModel", "vehicle", new Vehicle());
	}

	

	@PostMapping("/searchByModel")
	public String handleSearchByModel(Model model, @ModelAttribute("vehicle") Vehicle vehicle, HttpSession session) {

		System.out.println(vehicle.getModel());
		vehicle.setModel(vehicle.getModel());
		List<Vehicle> modelQuery = vehicleService.getVehiclesByModel(vehicle.getModel());
		System.out.println(modelQuery.size());
		
		if (!modelQuery.isEmpty()) {
			for (Vehicle temp : modelQuery) {
				System.out.println(temp);
			}
			model.addAttribute("vehicles", modelQuery);
			return "searchByModel";
		} else {
			System.out.println("No models found ");
			return "home";
		}

	}
	 

	// TODO sell car functionality: should remove from available inventory

	// TODO bonus: add new car to inventory including photo
	
	
}
