package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Rollback(value = false)
class RoleRepositoryTest {
	@Autowired
	private RoleRepository roleRepository;

	@Test
	public void testCreateRole() {
		Role roleAdmin = new Role("Admin", "Manage everything");
		roleRepository.save(roleAdmin);
	}

	@Test
	public void testRestRole() {
		Role roleSalesPerson = new Role("SalesPerson", "Manage product price, customers, shipping, orders and sales report");
		Role roleEditor = new Role("Editor", "Manage categories, brands, products, articles and menu");
		Role roleShipper = new Role("Shipper", "View products, view orders and update order status");
		Role roleAssistant = new Role("Assistant", "Manage questions and reviews");

		roleRepository.saveAll(List.of(roleSalesPerson,roleEditor, roleShipper, roleAssistant));
	}
}
