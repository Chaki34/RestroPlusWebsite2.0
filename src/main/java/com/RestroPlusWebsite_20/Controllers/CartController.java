package com.RestroPlusWebsite_20.Controllers;

import com.RestroPlusWebsite_20.Entities.MenuCategoryDetails;
import com.RestroPlusWebsite_20.Entities.ShoppingCart;
import com.RestroPlusWebsite_20.Entities.UserEntity;
import com.RestroPlusWebsite_20.repos.MenuCategoryDetailsRepository;
import com.RestroPlusWebsite_20.repos.ShoppingCartRepository;
import com.RestroPlusWebsite_20.repos.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final ShoppingCartRepository cartRepository;
    private final MenuCategoryDetailsRepository detailsRepository;
    private final UserRepository userRepository;

    public CartController(
            ShoppingCartRepository cartRepository,
            MenuCategoryDetailsRepository detailsRepository,
            UserRepository userRepository
    ) {
        this.cartRepository = cartRepository;
        this.detailsRepository = detailsRepository;
        this.userRepository = userRepository;
    }

    /*
     ==========================================
                ADD TO CART
     ==========================================
    */

    @PostMapping("/add/{id}")
    @ResponseBody
    public String addToCart(
            @PathVariable Long id,
            HttpSession session
    ) {

        Long phone =
                (Long) session.getAttribute("loggedInPhone");

        if (phone == null) {
            return "LOGIN";
        }

        UserEntity user =
                userRepository.findByPhoneno(phone)
                        .orElseThrow(() ->
                                new RuntimeException("User not found")
                        );

        MenuCategoryDetails details =
                detailsRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Food item not found")
                        );

        Optional<ShoppingCart> existingCart =
                cartRepository.findByUserAndMenuCategoryDetails(
                        user,
                        details
                );

        ShoppingCart cart;

        /*
         ==========================================
                    IF ITEM ALREADY EXISTS
         ==========================================
        */

        if (existingCart.isPresent()) {

            cart = existingCart.get();

            cart.setQuantity(
                    cart.getQuantity() + 1
            );

        }

        /*
         ==========================================
                    NEW CART ITEM
         ==========================================
        */

        else {

            cart = new ShoppingCart();

            cart.setUser(user);

            cart.setMenuCategoryDetails(details);

            cart.setQuantity(1);
        }

        /*
         ==========================================
                    PRICE CALCULATION
         ==========================================
        */

        cart.setItemPrice(details.getPrice());

        cart.setTotalPrice(
                cart.getQuantity() * details.getPrice()
        );

        cartRepository.save(cart);

        return String.valueOf(cart.getQuantity());
    }

    /*
     ==========================================
            UPDATE QUANTITY AJAX
     ==========================================
    */

    @PostMapping("/update/{foodId}/{action}")
    @ResponseBody
    public String updateQuantity(
            @PathVariable Long foodId,
            @PathVariable String action,
            HttpSession session
    ) {

        try {

            Long phone =
                    (Long) session.getAttribute("loggedInPhone");

            if (phone == null) {
                return "LOGIN";
            }

            UserEntity user =
                    userRepository.findByPhoneno(phone)
                            .orElseThrow(() ->
                                    new RuntimeException("User not found")
                            );

            MenuCategoryDetails details =
                    detailsRepository.findById(foodId)
                            .orElseThrow(() ->
                                    new RuntimeException("Food item not found")
                            );

            Optional<ShoppingCart> optionalCart =
                    cartRepository.findByUserAndMenuCategoryDetails(
                            user,
                            details
                    );

            /*
             ==========================================
                    IF ITEM NOT FOUND IN CART
             ==========================================
            */

            if (optionalCart.isEmpty()) {

                /*
                    IF MINUS CLICKED
                */

                if (action.equals("minus")) {
                    return "DELETED";
                }

                /*
                    CREATE NEW CART
                */

                ShoppingCart newCart =
                        new ShoppingCart();

                newCart.setUser(user);

                newCart.setMenuCategoryDetails(details);

                newCart.setQuantity(1);

                newCart.setItemPrice(details.getPrice());

                newCart.setTotalPrice(
                        details.getPrice()
                );

                cartRepository.save(newCart);

                return "1";
            }

            ShoppingCart cart =
                    optionalCart.get();

            int qty =
                    cart.getQuantity();

            /*
             ==========================================
                            PLUS
             ==========================================
            */

            if (action.equals("plus")) {
                qty++;
            }

            /*
             ==========================================
                            MINUS
             ==========================================
            */

            if (action.equals("minus")) {
                qty--;
            }

            /*
             ==========================================
                        DELETE IF ZERO
             ==========================================
            */

            if (qty <= 0) {

                cartRepository.delete(cart);

                return "DELETED";
            }

            /*
             ==========================================
                        UPDATE CART
             ==========================================
            */

            cart.setQuantity(qty);

            cart.setItemPrice(details.getPrice());

            cart.setTotalPrice(
                    qty * details.getPrice()
            );

            cartRepository.save(cart);

            return String.valueOf(qty);

        } catch (Exception e) {

            e.printStackTrace();

            return "ERROR";
        }
    }

    /*
     ==========================================
                VIEW CART PAGE
     ==========================================
    */

    @GetMapping("/view")
    public String viewCart(
            HttpSession session,
            Model model
    ) {

        Long phone =
                (Long) session.getAttribute("loggedInPhone");

        if (phone == null) {
            return "redirect:/auth/customer";
        }

        UserEntity user =
                userRepository.findByPhoneno(phone)
                        .orElseThrow(() ->
                                new RuntimeException("User not found")
                        );

        List<ShoppingCart> cartItems =
                cartRepository.findByUser(user);

        double grandTotal =
                cartItems.stream()
                        .mapToDouble(
                                ShoppingCart::getTotalPrice
                        )
                        .sum();

        model.addAttribute(
                "cartItems",
                cartItems
        );

        model.addAttribute(
                "grandTotal",
                grandTotal
        );

        return "shopping-cart";
    }

    /*
     ==========================================
            UPDATE QUANTITY FROM PAGE
     ==========================================
    */

    @GetMapping("/update-page/{id}/{action}")
    public String updatePageQuantity(
            @PathVariable Long id,
            @PathVariable String action
    ) {

        try {

            ShoppingCart cart =
                    cartRepository.findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Cart item not found"
                                    )
                            );

            int qty =
                    cart.getQuantity();

            /*
             ==========================================
                            PLUS
             ==========================================
            */

            if (action.equals("plus")) {
                qty++;
            }

            /*
             ==========================================
                            MINUS
             ==========================================
            */

            if (action.equals("minus")) {
                qty--;
            }

            /*
             ==========================================
                        DELETE IF ZERO
             ==========================================
            */

            if (qty <= 0) {

                cartRepository.delete(cart);

                return "redirect:/cart/view";
            }

            /*
             ==========================================
                        UPDATE CART
             ==========================================
            */

            Double itemPrice =
                    cart.getMenuCategoryDetails()
                            .getPrice();

            cart.setQuantity(qty);

            cart.setItemPrice(itemPrice);

            cart.setTotalPrice(
                    qty * itemPrice
            );

            cartRepository.save(cart);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return "redirect:/cart/view";
    }

}