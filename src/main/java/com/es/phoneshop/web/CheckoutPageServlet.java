package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.DefaultOrderService;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderService;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.validator.Validator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CheckoutPageServlet extends AbstractServlet {

    private OrderService orderService;
    private Validator validator;
    private String parseDateString = "dd-MM-yyyy";
    private String emptyFieldString = "Field must not be empty";
    private String incorrectInputString = "Incorrect input format";
    private String incorrectDate = "Incorrect date";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        orderService = DefaultOrderService.getInstance();
        validator = Validator.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = serviceGetter.getCart(request);
        if (cart.getCartItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?errorMessage=Cart is empty");
            return;
        }
        request.setAttribute("order", orderService.getOrder(cart));
        request.setAttribute("paymentMethods", orderService.getPaymentMethods());
        request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> errors = new HashMap<>();
        Order order = orderService.getOrder(serviceGetter.getCart(request));
        stringParameterValidation(request, "firstName", errors,
                order.getOrderDetails()::setFirstName, validator.validateNameOrSurname);
        stringParameterValidation(request, "lastName", errors,
                order.getOrderDetails()::setLastName, validator.validateNameOrSurname);
        stringParameterValidation(request, "phone", errors,
                order.getOrderDetails()::setPhone, validator.validatePhone);
        dateParameterValidation(request, "deliveryDate", errors,
                order.getOrderDetails()::setDeliveryDate, validator.validateDate);
        stringParameterValidation(request, "deliveryAddress", errors,
                order.getOrderDetails()::setDeliveryAddress, validator.validateAddress);
        paymentMethodParameterCheckEmpty(request, errors, order);
        handleErrors(request, response, errors, order);

    }

    private void handleErrors(HttpServletRequest request, HttpServletResponse response,
                              Map<String, String> errors, Order order) throws IOException, ServletException {
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("order", order);
            request.setAttribute("paymentMethods", orderService.getPaymentMethods());
            request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
        } else {
            orderService.placeOrder(order);
            cartService.clear(serviceGetter.getCart(request));
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());
        }
    }

    private void stringParameterValidation(HttpServletRequest request, String parameter, Map<String, String> errors,
                                           Consumer<String> consumer, Predicate<String> validate) {
        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            errors.put(parameter, emptyFieldString);
            return;
        }
        if (!validate.test(value)) {
            errors.put(parameter, incorrectInputString);
            return;
        }
        consumer.accept(value);
    }

    private void dateParameterValidation(HttpServletRequest request, String parameter, Map<String, String> errors,
                                         Consumer<Date> consumer, Predicate<String> validate) {
        String value = request.getParameter(parameter);
        Date date;
        if (!validate.test(value)) {
            errors.put(parameter, "Required date format: " + parseDateString);
            return;
        }
        try {
            date = new SimpleDateFormat(parseDateString).parse(value);
            consumer.accept(date);
        } catch (NullPointerException | ParseException e) {
            errors.put(parameter, incorrectDate);
        }
    }

    private void paymentMethodParameterCheckEmpty(HttpServletRequest request,
                                                  Map<String, String> errors, Order order) {
        String value = request.getParameter("paymentMethod");
        if (value == null || value.isEmpty()) {
            errors.put("paymentMethod", emptyFieldString);
        } else {
            order.getOrderDetails().setPaymentMethod(PaymentMethod.valueOf(value));
        }
    }
}
