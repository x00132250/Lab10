package controllers;

import play.api.Environment;
import play.mvc.*;
import play.data.*;
import play.db.ebean.Transactional;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import views.html.*;

// Import models
import models.*;


public class HomeController extends Controller {

    // Declare a private FormFactory instance
    private FormFactory formFactory;

    //  Inject an instance of FormFactory it into the controller via its constructor
    @Inject
    public HomeController(FormFactory f) {
        this.formFactory = f;
    }

    public Result index(String name) {

        return ok(index.render("Welcome to the Home page", name));
    }

    public Result about() {

        return ok(about.render());
    }

    public Result products(Long cat) {

        // Get list of all categories in ascending order
        List<Category> categoriesList = Category.findAll();
        List<Product> productsList = new ArrayList<Product>();


        if (cat == 0) {
            productsList = Product.findAll();
        } else {
            productsList = Category.find.ref(cat).getProducts();
        }

        return ok(products.render(productsList, categoriesList));
    }

    // Render and return  the add new product page
    // The page will load and display an empty add product form
    public Result addProduct() {

        // Create a form by wrapping the Product class
        // in a FormFactory form instance
        Form<Product> addProductForm = formFactory.form(Product.class);

        // Render the Add Product View, passing the form object   
        return ok(addProduct.render(addProductForm));
    }

    public Result addProductSubmit() {

        // Create a product form object (to hold submitted data)
        // 'Bind' the object to the submitted form (this copies the filled form)
        Form<Product> newProductForm = formFactory.form(Product.class).bindFromRequest();

        // Check for errors (based on Product class annotations)
        if(newProductForm.hasErrors()) {
            // Display the form again
            return badRequest(addProduct.render(newProductForm));
        }

        // Extract the product from the form object
        Product p = newProductForm.get();

        if (p.getId() == null) {
            //Save to the database via Ebean (remember Product class extends Model)
            p.save();
        }
        //Product already exists so update
        else if (p.getId() != null) {
            p.update();
        }

        // Set a success message in temporary flash
        // for display in return view
        flash("success", "Product " + p.getName() + " has been created");

        // Redirect to the admin home
        return redirect(controllers.routes.HomeController.products(0));
    }

    // Delete Product by id
    public Result deleteProduct(Long id) {

        // find product by id and call delete method
        Product.find.ref(id).delete();
        // Add message to flash session
        flash("success", "Product has been deleted");

        // Redirect to products page
        return redirect(routes.HomeController.products(0));
    }

    //Update a product by ID
    //called when edit button is pressed
    @Transactional
    public Result updateProduct(Long id) {

        Product p;
        Form<Product> productForm;

        try {
            //Find product by ID
            p = Product.find.byId(id);

            //Create a form based on the Product class and fill
            productForm = formFactory.form(Product.class).fill(p);
        } catch (Exception ex) {
            return badRequest("error");
        }

        return ok(addProduct.render(productForm));
    }


}
