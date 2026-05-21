package com.RestroPlusWebsite_20.Controllers;

import com.RestroPlusWebsite_20.Entities.MenuCategoryDetails;
import com.RestroPlusWebsite_20.Entities.OrderEntity;
import com.RestroPlusWebsite_20.Entities.ShoppingCart;
import com.RestroPlusWebsite_20.Entities.UserEntity;
import com.RestroPlusWebsite_20.repos.MenuCategoryDetailsRepository;
import com.RestroPlusWebsite_20.repos.OrderRepository;
import com.RestroPlusWebsite_20.repos.ShoppingCartRepository;
import com.RestroPlusWebsite_20.repos.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final ShoppingCartRepository cartRepository;
    private final MenuCategoryDetailsRepository detailsRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public CartController(
            ShoppingCartRepository cartRepository,
            MenuCategoryDetailsRepository detailsRepository,
            UserRepository userRepository, OrderRepository orderRepository
    ) {
        this.cartRepository = cartRepository;
        this.detailsRepository = detailsRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
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

    /*
     ==========================================
                ACTIVE CART ITEMS
     ==========================================
    */

        List<ShoppingCart> cartItems =
                cartRepository
                        .findByUserAndSavedForLaterFalseAndOrderedFalse(user);

    /*
     ==========================================
                SAVED CART ITEMS
     ==========================================
    */

        List<ShoppingCart> savedCartItems =
                cartRepository
                        .findByUserAndSavedForLaterTrue(user);

    /*
     ==========================================
                GRAND TOTAL
     ==========================================
    */

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
                "savedCartItems",
                savedCartItems
        );

        model.addAttribute(
                "grandTotal",
                grandTotal
        );

        model.addAttribute(
                "user",
                user
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

    /*
 ==========================================
        SAVE CART FOR LATER
 ==========================================
*/

    @GetMapping("/save/{id}")
    public String saveCartItem(
            @PathVariable Long id
    ) {

        ShoppingCart cart =
                cartRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Cart item not found"
                                )
                        );

        cart.setSavedForLater(true);

        cartRepository.save(cart);

        return "redirect:/cart/view";
    }

    /*
 ==========================================
        MOVE TO ACTIVE CART
 ==========================================
*/

    @GetMapping("/move-to-cart/{id}")
    public String moveToCart(
            @PathVariable Long id
    ) {

        ShoppingCart cart =
                cartRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Cart item not found"
                                )
                        );

        cart.setSavedForLater(false);

        cartRepository.save(cart);

        return "redirect:/cart/view";
    }

    /*
 ==========================================
        SAVE ALL CART ITEMS
 ==========================================
*/

    @GetMapping("/save-all")
    public String saveAllCartItems(HttpSession session) {

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
                cartRepository
                        .findByUserAndSavedForLaterFalseAndOrderedFalse(user);

        for (ShoppingCart cart : cartItems) {

            cart.setSavedForLater(true);

            cartRepository.save(cart);
        }

        return "redirect:/cart/view";
    }

/*
 ==========================================
            CLEAR ACTIVE CART
 ==========================================
*/

    @GetMapping("/clear")
    public String clearCart(HttpSession session) {

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
                cartRepository
                        .findByUserAndSavedForLaterFalseAndOrderedFalse(user);

        cartRepository.deleteAll(cartItems);

        return "redirect:/cart/view";
    }

/*
 ==========================================
        REMOVE SINGLE CART ITEM
 ==========================================
*/

    @GetMapping("/remove/{id}")
    public String removeCartItem(
            @PathVariable Long id
    ) {

        ShoppingCart cart =
                cartRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Cart item not found"
                                )
                        );

        cartRepository.delete(cart);

        return "redirect:/cart/view";
    }

/*
 ==========================================
        DELETE SAVED CART ITEM
 ==========================================
*/

    @GetMapping("/delete-saved/{id}")
    public String deleteSavedCart(
            @PathVariable Long id
    ) {

        ShoppingCart cart =
                cartRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Saved cart item not found"
                                )
                        );

        cartRepository.delete(cart);

        return "redirect:/cart/view";
    }

    /*
 ==========================================
            CHECKOUT PAGE
 ==========================================
*/

    @GetMapping("/checkout")
    public String checkoutPage(
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
                cartRepository
                        .findByUserAndSavedForLaterFalseAndOrderedFalse(user);

        double grandTotal =
                cartItems.stream()
                        .mapToDouble(ShoppingCart::getTotalPrice)
                        .sum();

        model.addAttribute("user", user);

        model.addAttribute("cartItems", cartItems);

        model.addAttribute("grandTotal", grandTotal);

        return "checkout-page";
    }

    /*
 ==========================================
            PLACE ORDER
 ==========================================
*/

    @PostMapping("/place-order")
    public String placeOrder(
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
                cartRepository
                        .findByUserAndSavedForLaterFalseAndOrderedFalse(user);

        double grandTotal =
                cartItems.stream()
                        .mapToDouble(ShoppingCart::getTotalPrice)
                        .sum();

    /*
     ==========================================
                CREATE ORDER
     ==========================================
    */

        OrderEntity order =
                new OrderEntity();

        order.setUser(user);

        order.setCustomerName(
                user.getUsername()
        );

        order.setPhoneNumber(
                user.getPhoneno()
        );

        order.setCity(
                user.getCity()
        );

        order.setGrandTotal(
                grandTotal
        );

        order.setTotalItems(
                cartItems.size()
        );

        order.setOrderStatus(
                "PLACED"
        );

        order.setOrderDate(
                LocalDateTime.now()
        );

        orderRepository.save(order);

    /*
     ==========================================
            MARK CART ITEMS ORDERED
     ==========================================
    */

        for (ShoppingCart cart : cartItems) {

            cart.setOrdered(true);

            cartRepository.save(cart);
        }

        model.addAttribute("order", order);

        model.addAttribute("user", user);

        model.addAttribute("cartItems", cartItems);

        model.addAttribute("grandTotal", grandTotal);

        return "order-success";
    }

}