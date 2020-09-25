package com.m2.drools;

import java.util.ArrayList;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;


/**
 *  Drools is a Business Logic integration Platform (BLiP). Its standard way to handle the business logic
 *Format:
 *
 *rule  <rule_name>
 *    <attribute> <value>
 *
 * when
 *       <conditions>
 *
 * then
 *       <actions>
 * end
 *
 *
 *
 */
public class DroolsRuleEngineExample {

    public static final void main(String[] args) {
        try {
            // load up the knowledge base
            KieServices ks = KieServices.Factory.get();
            KieContainer kContainer = ks.getKieClasspathContainer();
            KieSession kSession = kContainer.newKieSession("ksession-rules");

            Customer customer = Customer.newCustomer("Mintu Choudhary");
    		Product p1 = new Product("Laptop", 15000); // default available 5 quantity
    		Product p2 = new Product("Mobile", 5000);
    		p2.setRequiresRegistration(true);
    		Product p3 = new Product("Books", 2000);
    		
    		Product p4OutOfStock = new Product("TV", 2000);
    		p4OutOfStock.setAvailableQty(3);
    		
    		Product p5 = new Product("Tabs", 10000);
    		p5.setAvailableQty(2);
    		
    		customer.addItem(p1, 1);
    		customer.addItem(p2, 2);
    		customer.addItem(p3, 5);
			customer.addItem(p4OutOfStock, 0);
    		customer.setCoupon("DISC01");

    		List<CartItem> cartItems = customer.getCart().getCartItems();
    		for (CartItem cartItem: cartItems) {
    			kSession.insert(cartItem);
    		}
    		System.out.println("************* Fire Rules **************");
            kSession.fireAllRules(); 
            System.out.println("************************************");
           // System.out.println("Customer cart\n" + customer);
            
            Customer newCustomer = Customer.newCustomer("JOHN01");
    		newCustomer.addItem(p1, 1);
    		newCustomer.addItem(p2, 2);
    		newCustomer.addItem(p4OutOfStock, 1);
    		newCustomer.addItem(p5, 10);    		
    		
    		cartItems = newCustomer.getCart().getCartItems();
    		for (CartItem cartItem: cartItems) {
    			kSession.insert(cartItem);
    		}
    		kSession.insert(newCustomer.getCart());
    		kSession.setGlobal("outOfStockProducts", new ArrayList<Product>());
    		System.out.println("************* Fire Rules **************");
            kSession.fireAllRules();
            System.out.println("************************************");
           // System.out.println("Customer cart\n" + customer);
                        
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
/**
 * ************* Fire Rules **************
 *
 * Has customer registered for the product? Rule
 * ************************************
 * Can't process product: Mobile, price: 5000, as requires registration. Customer not registered for the product!
 *
 * Is Out-Of Stock Rule
 * ************************************
 * Can't process as TV is Out-Of-Stock
 *
 * If has coupon, 5% discount Rule
 * ************************************
 * Coupon Rule: Process product: Books, price: 2000, qty 5, apply discount 1250.0
 *
 * If has coupon, 5% discount Rule
 * ************************************
 * Coupon Rule: Process product: Laptop, price: 15000, qty 1, apply discount 1250.0
 *
 * If new, 2% discount Rule
 * ************************************
 * New Customer Rule: Process product: Books, price: 2000, qty 5, apply discount 5000.0
 *
 * If new, 2% discount Rule
 * ************************************
 * New Customer Rule: Process product: Laptop, price: 15000, qty 1, apply discount 5000.0
 * ************************************
 * ************* Fire Rules **************
 * ************************************
 */
