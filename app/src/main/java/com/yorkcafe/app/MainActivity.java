package com.yorkcafe.app;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.FrameLayout;
import android.widget.EditText;
import android.os.Handler;
import android.os.Looper;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.content.res.ColorStateList;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.button.MaterialButton;

import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CafeSkateAppMainActivity";
    private static final long SLIDER_DELAY_MS = 5000;

    // Tab Pages
    private View pageHome, pageFood, pageCart, pageProfile;
    private ViewPager2 mainViewPager;

    // Custom Bottom Nav Bar Elements
    private FrameLayout btnNavHome, btnNavMenu, btnNavCart, btnNavProfile;
    private ImageView imgNavHome, imgNavMenu, imgNavCart, imgNavProfile;

    // Header Shopping Bag Cart button and Badge
    private FrameLayout btnHeaderCart;
    private TextView txtCartBadge;

    // Hero Section Views
    private CardView heroImageCard;
    private ViewPager2 heroImagePager;
    private ImageView heartIconHero, starIconHero;

    // Category Filter Buttons
    private MaterialButton btnCoffee, btnNonCoffee, btnSnack;
    private String currentHomeFilter = "ALL";

    // Polaroid Home Cards
    private CardView polaroidCard;
    private ImageView coffeePourImage;
    
    private View mangoSugusHomeCard;
    private ImageView mangoSugusHomeImage;
    private MaterialButton btnMangoSugusHomeAdd;

    private View kopiSusuHomeCard;
    private ImageView kopiSusuHomeImage;
    private MaterialButton btnKopiSusuHomeAdd;

    private View avocadoCoffeeHomeCard;
    private ImageView avocadoCoffeeHomeImage;
    private MaterialButton btnAvocadoCoffeeHomeAdd;

    private View gorohoStickHomeCard;
    private ImageView gorohoStickHomeImage;
    private MaterialButton btnGorohoStickHomeAdd;

    private View mixPlatterHomeCard;
    private ImageView mixPlatterHomeImage;
    private MaterialButton btnMixPlatterHomeAdd;

    // Food Tab Favorite Cards
    private View cardFav1, cardFav2, cardFav3;
    private MaterialButton btnFavAdd1, btnFavAdd2, btnFavAdd3;
    private ImageView imgFav1, imgFav2, imgFav3;

    // Food Tab Main Cards
    private View foodMainCard1, foodMainCard2, foodMainCard3;
    private MaterialButton btnFoodMainAdd1, btnFoodMainAdd2, btnFoodMainAdd3;
    private ImageView imgFoodMain1;

    // Food Tab Menu Category Lists
    private LinearLayout listCoffee, listAntiCoffee, listSpillTea, listJustCase, listMilkOption, gridBites, gridIndomie, gridMore;

    // Cart Tab elements
    private View layoutEmptyCart, layoutActiveCart;
    private LinearLayout cartItemsList;
    private TextView txtCartTotal;
    private MaterialButton btnCheckout;
    private MaterialButton btnJoinUs;

    // Dine In / Takeaway fields
    private MaterialButton btnTypeDineIn, btnTypeTakeaway;
    private LinearLayout layoutTableInput;
    private EditText inputTableNumber;
    private String selectedOrderType = ""; // "DINE_IN" or "TAKEAWAY"

    // OVERLAY: Product Detail Page
    private View pageDetail;
    private ImageView imgDetailProduct;
    private com.google.android.material.card.MaterialCardView btnDetailBack;
    private TextView txtDetailTitle, txtDetailDesc, txtDetailPrice;
    private MaterialButton btnSizeS, btnSizeM, btnSizeL;
    private MaterialButton btnTempIced, btnTempHot;
    private MaterialButton btnDetailTypeDineIn, btnDetailTypeTakeaway;
    private MaterialButton btnDetailGrab;
    private String selectedDetailSize = "M"; // S, M, L
    private String selectedDetailTemp = "ICED"; // ICED, HOT
    private String selectedDetailType = "DINE IN"; // DINE IN, TAKEAWAY
    private String selectedDetailPortion = "SINGLE"; // SINGLE, DOUBLE
    private int editingCartItemIndex = -1;
    private String detailProductName = "";
    private boolean isIndomieProduct = false;
    private int detailProductPrice = 0;

    // Ratings & Reviews
    private ImageView[] detailStars = new ImageView[5];
    private TextView txtSelectedRating;
    private EditText inputDetailComment;
    private MaterialButton btnSubmitReview;
    private LinearLayout layoutReviewsList;
    private int currentProductRating = 0;

    // Portion Selection (for Indomie)
    private TextView txtPortionLabel;
    private LinearLayout layoutPortionButtons;
    private MaterialButton btnPortionSingle, btnPortionDouble;

    private static class ProductReview {
        String productName;
        int rating;
        String comment;
        String username;

        ProductReview(String productName, int rating, String comment, String username) {
            this.productName = productName;
            this.rating = rating;
            this.comment = comment;
            this.username = username;
        }
    }
    private List<ProductReview> productReviewsList = new ArrayList<>();

    // Tab Navigation History
    private List<String> tabHistory = new ArrayList<>();

    // Empty Cart refuel button
    private MaterialButton btnEmptyCartRefuel;

    // OVERLAY: Checkout Receipt Page
    private View pageCheckoutFlow;
    private com.google.android.material.card.MaterialCardView btnCheckoutClose;
    private LinearLayout checkoutItemsContainer;
    private TextView txtCheckoutSubtotal;
    private TextView txtCheckoutMethod;
    private TextView txtCheckoutGiantTotal;
    private TextView txtCheckoutReceiptOrderNo;
    private com.google.android.material.card.MaterialCardView btnPayEpay, btnPayQr;
    private MaterialButton btnCheckoutProceed;
    private String selectedPaymentMethod = "E-PAY"; // E-PAY, QR
    private int currentOrderNumber = 100;

    // OVERLAY: Order Secured Screen
    private View pageSecured;
    private TextView txtSecuredOrderNo, txtSecuredTotal, txtSecuredMethod;
    private MaterialButton btnSecuredTrack;
    private TextView btnSecuredHome;

    // OVERLAY: Transaction Crashed Screen
    private View pageCrashed;
    private MaterialButton btnCrashedRetry;
    private TextView btnCrashedAbort;

    // PROFILE PAGE options
    private com.google.android.material.card.MaterialCardView btnPastOrders, btnSavedVibes, btnPaymentMethods, btnSettings;
    private FrameLayout profileCartBag;
    private TextView txtProfileUsername;
    private TextView txtProfileRole;
    private TextView btnLogout;
    private View btnJoinUsContainer;

    // Menu Item Structure
    private static class MenuItem {
        String name;
        int price;
        String desc;
        int imageResId;

        MenuItem(String name, int price, String desc, int imageResId) {
            this.name = name;
            this.price = price;
            this.desc = desc;
            this.imageResId = imageResId;
        }
    }

    // Drinks Categories Data
    private final List<MenuItem> coffeeList = Arrays.asList(
        new MenuItem("Americano", 25, "Rich espresso shot with hot water.", R.drawable.b2),
        new MenuItem("Cappuccino", 30, "Espresso with thick steamed milk foam.", R.drawable.b1),
        new MenuItem("Latte", 30, "Smooth espresso with steamed milk.", R.drawable.b2),
        new MenuItem("Vanilla Latte", 32, "Latte infused with sweet vanilla syrup.", R.drawable.b1),
        new MenuItem("Pandan Latte", 32, "Unique aromatic pandan infused latte.", R.drawable.b2),
        new MenuItem("Hazelnut Latte", 32, "Nutty sweet hazelnut latte.", R.drawable.b1),
        new MenuItem("Caramel Latte", 32, "Rich caramel-sweetened coffee latte.", R.drawable.b2),
        new MenuItem("Butterscotch Latte", 32, "Deep buttery notes mixed with espresso.", R.drawable.b1),
        new MenuItem("Tiramisu Latte", 32, "Classic Italian dessert-themed espresso latte.", R.drawable.b2)
    );

    private final List<MenuItem> antiCoffeeList = Arrays.asList(
        new MenuItem("Matcha Latte", 40, "Premium stone-ground Japanese green tea.", R.drawable.b1),
        new MenuItem("Avocado Milk", 30, "Thick sweet creamy avocado blended with fresh milk.", R.drawable.b2),
        new MenuItem("Chocolate", 35, "Rich, dark, premium cocoa drink.", R.drawable.b1),
        new MenuItem("Taro", 28, "Sweet earthy purple taro drink.", R.drawable.b2),
        new MenuItem("Red Velvet", 28, "Creamy red velvet cocoa dream.", R.drawable.b1),
        new MenuItem("Thai Tea", 30, "Authentic sweet brewed Thai tea milk.", R.drawable.b2)
    );

    private final List<MenuItem> spillTeaList = Arrays.asList(
        new MenuItem("Hot Tea", 20, "Steeping hot black tea blend.", R.drawable.b2),
        new MenuItem("Iced Tea", 20, "Refreshing sweetened iced black tea.", R.drawable.b1),
        new MenuItem("Lemon Tea", 22, "Zesty lemon infused iced black tea.", R.drawable.b2),
        new MenuItem("Lychee Tea", 22, "Sweet lychee syrup and whole fruit iced tea.", R.drawable.b1),
        new MenuItem("Mango Tea", 22, "Tropical mango notes iced tea.", R.drawable.b2)
    );

    private final List<MenuItem> justCaseList = Arrays.asList(
        new MenuItem("Extra Shot", 5, "Double up your caffeine dose.", R.drawable.b2),
        new MenuItem("Extra Syrup", 8, "Sweet addition to your taste buds.", R.drawable.b1),
        new MenuItem("Extra Sauce", 8, "Creamy rich toppings.", R.drawable.b2),
        new MenuItem("ColdFoam", 8, "Silky cold sweet milk topping.", R.drawable.b1)
    );

    private final List<MenuItem> milkOptionList = Arrays.asList(
        new MenuItem("Almond Milk", 12, "Plant-based nutty milk substitute.", R.drawable.b1),
        new MenuItem("Oat Milk", 8, "Creamy oat milk substitute.", R.drawable.b2)
    );

    private final List<MenuItem> bitesList = Arrays.asList(
        new MenuItem("Mix Plater", 35, "Kentang, nugget, sosis platters.", R.drawable.york),
        new MenuItem("Kentang Goreng", 20, "Crispy golden french fries.", R.drawable.york),
        new MenuItem("Pisang Goreng Tepung", 20, "Crispy flour-fried banana.", R.drawable.york),
        new MenuItem("Goroho Stick", 20, "Traditional crispy fried goroho banana stick.", R.drawable.york),
        new MenuItem("Roti Bakar Cokelat", 20, "Toasted bread with chocolate toppings.", R.drawable.york),
        new MenuItem("Roti Bakar Keju", 20, "Toasted bread with grated cheese toppings.", R.drawable.york),
        new MenuItem("Roti Bakar Stroberi", 20, "Toasted bread with strawberry jam toppings.", R.drawable.york),
        new MenuItem("Roti Bakar Cokelat Keju", 25, "Toasted bread with chocolate and cheese toppings.", R.drawable.york)
    );

    private final List<MenuItem> indomieList = Arrays.asList(
        new MenuItem("Indomie Goreng", 15, "Classic fried Indomie noodles. Choose Single or Double.", R.drawable.york),
        new MenuItem("Indomie Soto", 15, "Warm soto flavored Indomie noodle soup (kuah).", R.drawable.york),
        new MenuItem("Indomie Ayam Bawang", 15, "Savory chicken onion Indomie noodle soup (kuah).", R.drawable.york)
    );

    private final List<MenuItem> moreList = Arrays.asList(
        new MenuItem("Extra Telur", 5, "Add extra egg to your food.", R.drawable.york),
        new MenuItem("Extra Nasi", 5, "Add extra rice to your food.", R.drawable.york),
        new MenuItem("Extra Sosis", 7, "Add extra sausage to your food.", R.drawable.york),
        new MenuItem("Extra Keju", 7, "Add extra cheese toppings.", R.drawable.york),
        new MenuItem("Extra Sambal", 8, "Add extra spicy sambal.", R.drawable.york)
    );

    // Cart State Management
    private static class CartItem {
        String name;
        int unitPrice;
        int quantity;

        CartItem(String name, int unitPrice, int quantity) {
            this.name = name;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
        }
    }
    private List<CartItem> cartItems = new ArrayList<>();

    // Auto-sliding handler
    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            if (heroImagePager != null && heroImagePager.getAdapter() != null) {
                int nextItem = heroImagePager.getCurrentItem() + 1;
                if (nextItem >= heroImagePager.getAdapter().getItemCount()) {
                    nextItem = 0;
                }
                heroImagePager.setCurrentItem(nextItem, true);
                sliderHandler.postDelayed(this, SLIDER_DELAY_MS);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initializeViews();
        setupCategoryFiltersOnClickListeners();
        setupHeroOnClickListeners();
        setupNavigationClickListeners();
        setupFoodTabOnClickListeners();
        setupCartActions();
        setupDetailOverlayActions();
        setupCheckoutOverlayActions();
        setupSecuredAndCrashedActions();
        setupProfileActions();

        // No grayscale filters applied - keeping all images in full color!

        // Populate dynamic lists
        populateMenuCategory(listCoffee, coffeeList);
        populateMenuCategory(listAntiCoffee, antiCoffeeList);
        populateMenuCategory(listSpillTea, spillTeaList);
        populateMenuCategory(listJustCase, justCaseList);
        populateMenuCategory(listMilkOption, milkOptionList);
        populateStraightGrid(gridBites, bitesList);
        populateStraightGrid(gridIndomie, indomieList);
        populateStraightGrid(gridMore, moreList);

        // Set active user name in profile
        android.content.SharedPreferences prefs = getSharedPreferences("YorkCafePrefs", MODE_PRIVATE);
        if (!prefs.contains("active_user_name")) {
            prefs.edit().putString("active_user_name", "Yorkers").apply();
        }
        updateUserSessionUI();

        selectTab("HOME");
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, SLIDER_DELAY_MS);

        updateUserSessionUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    private void updateUserSessionUI() {
        android.content.SharedPreferences prefs = getSharedPreferences("YorkCafePrefs", MODE_PRIVATE);
        String activeName = prefs.getString("active_user_name", "Yorkers");
        if (txtProfileUsername != null) {
            txtProfileUsername.setText(activeName);
        }
        if (txtProfileRole != null) {
            if (activeName.equalsIgnoreCase("Yorkers")) {
                txtProfileRole.setText("GUEST");
            } else {
                txtProfileRole.setText("MEMBER");
            }
        }
        if (btnJoinUsContainer != null) {
            if (activeName.equalsIgnoreCase("Yorkers")) {
                btnJoinUsContainer.setVisibility(View.VISIBLE);
            } else {
                btnJoinUsContainer.setVisibility(View.GONE);
            }
        } else if (btnJoinUs != null) {
            if (activeName.equalsIgnoreCase("Yorkers")) {
                btnJoinUs.setVisibility(View.VISIBLE);
            } else {
                btnJoinUs.setVisibility(View.GONE);
            }
        }
    }

    private void initializeViews() {
        // Tab Pages
        pageHome = findViewById(R.id.page_home);
        pageFood = findViewById(R.id.page_food);
        pageCart = findViewById(R.id.page_cart);
        pageProfile = findViewById(R.id.page_profile);

        if (pageHome != null) pageHome.setVisibility(View.VISIBLE);
        if (pageFood != null) pageFood.setVisibility(View.VISIBLE);
        if (pageCart != null) pageCart.setVisibility(View.VISIBLE);
        if (pageProfile != null) pageProfile.setVisibility(View.VISIBLE);

        mainViewPager = findViewById(R.id.main_view_pager);
        List<View> pages = new ArrayList<>();
        if (pageHome != null) pages.add(pageHome);
        if (pageFood != null) pages.add(pageFood);
        if (pageCart != null) pages.add(pageCart);
        if (pageProfile != null) pages.add(pageProfile);

        ViewPagerAdapter pageAdapter = new ViewPagerAdapter(pages);
        if (mainViewPager != null) {
            mainViewPager.setAdapter(pageAdapter);
            mainViewPager.setOffscreenPageLimit(3);
            mainViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    updateNavUIForPosition(position);

                    String tab = "HOME";
                    if (position == 1) tab = "FOOD";
                    else if (position == 2) tab = "CART";
                    else if (position == 3) tab = "PROFILE";

                    if (!tabHistory.isEmpty() && tabHistory.get(tabHistory.size() - 1).equals(tab)) {
                        // Already top
                    } else {
                        tabHistory.remove(tab);
                        tabHistory.add(tab);
                    }
                }
            });
        }

        // Custom Bottom Nav
        btnNavHome = findViewById(R.id.btn_nav_home);
        btnNavMenu = findViewById(R.id.btn_nav_menu);
        btnNavCart = findViewById(R.id.btn_nav_cart);
        btnNavProfile = findViewById(R.id.btn_nav_profile);

        imgNavHome = findViewById(R.id.img_nav_home);
        imgNavMenu = findViewById(R.id.img_nav_menu);
        imgNavCart = findViewById(R.id.img_nav_cart);
        imgNavProfile = findViewById(R.id.img_nav_profile);

        // Header Cart bag & badge
        btnHeaderCart = findViewById(R.id.btn_header_cart);
        txtCartBadge = findViewById(R.id.txt_cart_badge);

        // Hero Section
        heroImageCard = findViewById(R.id.hero_image_card);
        heroImagePager = findViewById(R.id.hero_image_pager);
        heartIconHero = null;
        starIconHero = null;

        List<Integer> heroImages = Arrays.asList(R.drawable.b1, R.drawable.b2, R.drawable.b3);
        heroImagePager.setAdapter(new HeroImageAdapter(heroImages, new HeroImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Do nothing
            }
        }));
        
        heroImagePager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });

        // Category Buttons
        btnCoffee = findViewById(R.id.btn_coffee_filter);
        btnNonCoffee = findViewById(R.id.btn_non_coffee_filter);
        btnSnack = findViewById(R.id.btn_snack_filter);

        // Polaroid Home Cards
        polaroidCard = findViewById(R.id.polaroid_card);
        coffeePourImage = findViewById(R.id.coffee_pour_image);
        
        mangoSugusHomeCard = findViewById(R.id.mango_sugus_container);
        mangoSugusHomeImage = findViewById(R.id.mango_sugus_home_image);
        btnMangoSugusHomeAdd = findViewById(R.id.mango_sugus_home_add_button);

        kopiSusuHomeCard = findViewById(R.id.kopi_susu_container);
        kopiSusuHomeImage = findViewById(R.id.kopi_susu_home_image);
        btnKopiSusuHomeAdd = findViewById(R.id.kopi_susu_home_add_button);

        avocadoCoffeeHomeCard = findViewById(R.id.avocado_coffee_container);
        avocadoCoffeeHomeImage = findViewById(R.id.avocado_coffee_home_image);
        btnAvocadoCoffeeHomeAdd = findViewById(R.id.avocado_coffee_home_add_button);

        gorohoStickHomeCard = findViewById(R.id.goroho_stick_container);
        gorohoStickHomeImage = findViewById(R.id.goroho_stick_home_image);
        btnGorohoStickHomeAdd = findViewById(R.id.goroho_stick_home_add_button);

        mixPlatterHomeCard = findViewById(R.id.mix_platter_container);
        mixPlatterHomeImage = findViewById(R.id.mix_platter_home_image);
        btnMixPlatterHomeAdd = findViewById(R.id.mix_platter_home_add_button);

        // Food Tab Favorite Cards
        cardFav1 = findViewById(R.id.card_fav_1);
        cardFav2 = findViewById(R.id.card_fav_2);
        cardFav3 = findViewById(R.id.card_fav_3);
        btnFavAdd1 = findViewById(R.id.btn_fav_add_1);
        btnFavAdd2 = findViewById(R.id.btn_fav_add_2);
        btnFavAdd3 = findViewById(R.id.btn_fav_add_3);
        imgFav1 = findViewById(R.id.img_fav_1);
        imgFav2 = findViewById(R.id.img_fav_2);
        imgFav3 = findViewById(R.id.img_fav_3);

        // Food Tab Main Cards
        foodMainCard1 = findViewById(R.id.food_main_card_1);
        foodMainCard2 = findViewById(R.id.food_main_card_2);
        foodMainCard3 = findViewById(R.id.food_main_card_3);
        btnFoodMainAdd1 = findViewById(R.id.btn_food_main_add_1);
        btnFoodMainAdd2 = findViewById(R.id.btn_food_main_add_2);
        btnFoodMainAdd3 = findViewById(R.id.btn_food_main_add_3);
        imgFoodMain1 = findViewById(R.id.img_food_main_1);

        // Food Tab Category Lists
        listCoffee = findViewById(R.id.list_coffee);
        listAntiCoffee = findViewById(R.id.list_anti_coffee);
        listSpillTea = findViewById(R.id.list_spill_tea);
        listJustCase = findViewById(R.id.list_just_case);
        listMilkOption = findViewById(R.id.list_milk_option);
        gridBites = findViewById(R.id.grid_bites);
        gridIndomie = findViewById(R.id.grid_indomie);
        gridMore = findViewById(R.id.grid_more);

        // Cart Tab Elements
        layoutEmptyCart = findViewById(R.id.layout_empty_cart);
        layoutActiveCart = findViewById(R.id.layout_active_cart);
        cartItemsList = findViewById(R.id.cart_items_list);
        txtCartTotal = findViewById(R.id.txt_cart_total);
        btnCheckout = findViewById(R.id.btn_checkout);

        btnTypeDineIn = findViewById(R.id.btn_type_dine_in);
        btnTypeTakeaway = findViewById(R.id.btn_type_takeaway);
        layoutTableInput = findViewById(R.id.layout_table_input);
        inputTableNumber = findViewById(R.id.input_table_number);

        // DETAIL OVERLAY views
        pageDetail = findViewById(R.id.page_detail);
        imgDetailProduct = findViewById(R.id.img_detail_product);
        btnDetailBack = findViewById(R.id.btn_detail_back);
        txtDetailTitle = findViewById(R.id.txt_detail_title);
        txtDetailDesc = findViewById(R.id.txt_detail_desc);
        txtDetailPrice = findViewById(R.id.txt_detail_price);
        btnSizeS = findViewById(R.id.btn_size_s);
        btnSizeM = findViewById(R.id.btn_size_m);
        btnSizeL = findViewById(R.id.btn_size_l);
        btnTempIced = findViewById(R.id.btn_temp_iced);
        btnTempHot = findViewById(R.id.btn_temp_hot);
        btnDetailTypeDineIn = null;
        btnDetailTypeTakeaway = null;
        btnDetailGrab = findViewById(R.id.btn_detail_grab);

        // Bind ratings & reviews
        detailStars[0] = findViewById(R.id.star_1);
        detailStars[1] = findViewById(R.id.star_2);
        detailStars[2] = findViewById(R.id.star_3);
        detailStars[3] = findViewById(R.id.star_4);
        detailStars[4] = findViewById(R.id.star_5);
        txtSelectedRating = findViewById(R.id.txt_selected_rating);
        inputDetailComment = findViewById(R.id.input_detail_comment);
        btnSubmitReview = findViewById(R.id.btn_submit_review);
        layoutReviewsList = findViewById(R.id.layout_reviews_list);

        // Bind empty cart refuel button
        btnEmptyCartRefuel = findViewById(R.id.btn_empty_cart_refuel);

        // Bind portion selection views
        txtPortionLabel = findViewById(R.id.txt_portion_label);
        layoutPortionButtons = findViewById(R.id.layout_portion_buttons);
        btnPortionSingle = findViewById(R.id.btn_portion_single);
        btnPortionDouble = findViewById(R.id.btn_portion_double);

        // Pre-populate mock reviews
        if (productReviewsList.isEmpty()) {
            productReviewsList.add(new ProductReview("House Drip", 5, "Best drip coffee in town! Highly recommend.", "SkatrBoi"));
            productReviewsList.add(new ProductReview("House Drip", 4, "Strong and bold, got me hyped.", "VibeCheck"));
            productReviewsList.add(new ProductReview("Oat Latte", 5, "Incredibly creamy and smooth. My favorite!", "OatMilkLover"));
            productReviewsList.add(new ProductReview("Kopi Susu Gula Aren", 5, "So sweet and perfect for hot days.", "RiderX"));
        }

        // CHECKOUT OVERLAY views
        pageCheckoutFlow = findViewById(R.id.page_checkout_flow);
        btnCheckoutClose = findViewById(R.id.btn_checkout_close);
        checkoutItemsContainer = findViewById(R.id.checkout_items_container);
        txtCheckoutSubtotal = findViewById(R.id.txt_checkout_subtotal);
        txtCheckoutMethod = findViewById(R.id.txt_checkout_method);
        txtCheckoutGiantTotal = findViewById(R.id.txt_checkout_giant_total);
        txtCheckoutReceiptOrderNo = findViewById(R.id.txt_checkout_receipt_order_no);
        btnPayEpay = findViewById(R.id.btn_pay_epay);
        btnPayQr = findViewById(R.id.btn_pay_qr);
        btnCheckoutProceed = findViewById(R.id.btn_checkout_proceed);

        // SECURED OVERLAY views
        pageSecured = findViewById(R.id.page_secured);
        txtSecuredOrderNo = findViewById(R.id.txt_secured_order_no);
        txtSecuredTotal = findViewById(R.id.txt_secured_total);
        txtSecuredMethod = findViewById(R.id.txt_secured_method);
        btnSecuredTrack = findViewById(R.id.btn_secured_track);
        btnSecuredHome = findViewById(R.id.btn_secured_home);

        // CRASHED OVERLAY views
        pageCrashed = findViewById(R.id.page_crashed);
        btnCrashedRetry = findViewById(R.id.btn_crashed_retry);
        btnCrashedAbort = findViewById(R.id.btn_crashed_abort);

        // PROFILE PAGE views
        btnPastOrders = findViewById(R.id.btn_past_orders);
        btnSavedVibes = findViewById(R.id.btn_saved_vibes);
        btnPaymentMethods = findViewById(R.id.btn_payment_methods);
        btnSettings = findViewById(R.id.btn_settings);
        profileCartBag = findViewById(R.id.profile_cart_bag);
        txtProfileUsername = findViewById(R.id.txt_profile_username);
        txtProfileRole = findViewById(R.id.txt_profile_role);
        btnLogout = findViewById(R.id.btn_logout);
        btnJoinUsContainer = findViewById(R.id.btn_join_us_container);

        // Bind Join us! button
        btnJoinUs = findViewById(R.id.btn_join_us);
        if (btnJoinUs != null) {
            btnJoinUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.content.Intent intent = new android.content.Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }

        // Keyboard visibility listener to hide detail_bottom_bar when keyboard is open
        final View rootView = findViewById(android.R.id.content);
        final View detailBottomBar = findViewById(R.id.detail_bottom_bar);
        if (rootView != null && detailBottomBar != null) {
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {
                private boolean isKeyboardOpen = false;

                @Override
                public void onGlobalLayout() {
                    android.graphics.Rect r = new android.graphics.Rect();
                    rootView.getWindowVisibleDisplayFrame(r);
                    int screenHeight = rootView.getRootView().getHeight();
                    int keypadHeight = screenHeight - r.bottom;

                    boolean isOpen = keypadHeight > screenHeight * 0.15;
                    if (isOpen != isKeyboardOpen) {
                        isKeyboardOpen = isOpen;
                        if (pageDetail != null && pageDetail.getVisibility() == View.VISIBLE) {
                            detailBottomBar.setVisibility(isOpen ? View.GONE : View.VISIBLE);
                        }
                    }
                }
            });
        }
    }

    private void setupCategoryFiltersOnClickListeners() {
        if (btnCoffee != null) {
            btnCoffee.setOnClickListener(v -> handleHomeFilterClick("COFFEE"));
        }
        if (btnNonCoffee != null) {
            btnNonCoffee.setOnClickListener(v -> handleHomeFilterClick("NON_COFFEE"));
        }
        if (btnSnack != null) {
            btnSnack.setOnClickListener(v -> handleHomeFilterClick("SNACK"));
        }
    }

    private void handleHomeFilterClick(String filter) {
        if (currentHomeFilter.equals(filter)) {
            applyHomeFilter("ALL");
        } else {
            applyHomeFilter(filter);
        }
    }

    private void applyHomeFilter(String filter) {
        currentHomeFilter = filter;

        // Reset default colors:
        // Coffee: yellow bg, black text
        if (btnCoffee != null) {
            btnCoffee.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.category_coffee_yellow)));
            btnCoffee.setTextColor(ContextCompat.getColor(this, R.color.black));
        }

        // Non-Coffee: blue bg, black text
        if (btnNonCoffee != null) {
            btnNonCoffee.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.category_non_coffee_blue)));
            btnNonCoffee.setTextColor(ContextCompat.getColor(this, R.color.black));
        }

        // Snack: pink bg, black text
        if (btnSnack != null) {
            btnSnack.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.category_skate_goods_pink)));
            btnSnack.setTextColor(ContextCompat.getColor(this, R.color.black));
        }

        // Highlight selected
        if (filter.equals("COFFEE") && btnCoffee != null) {
            btnCoffee.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
            btnCoffee.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if (filter.equals("NON_COFFEE") && btnNonCoffee != null) {
            btnNonCoffee.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
            btnNonCoffee.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if (filter.equals("SNACK") && btnSnack != null) {
            btnSnack.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
            btnSnack.setTextColor(ContextCompat.getColor(this, R.color.white));
        }

        // Show/hide home page items based on filter:
        // Basque Cheesecake: SNACK
        // Mango Sugus: NON_COFFEE
        // Kopi Susu Gula Aren: COFFEE
        // Avocado Coffee: COFFEE
        // Goroho Stick: SNACK
        // Mix Platter: SNACK
        int visCheesecake = (filter.equals("ALL") || filter.equals("SNACK")) ? View.VISIBLE : View.GONE;
        int visMangoSugus = (filter.equals("ALL") || filter.equals("NON_COFFEE")) ? View.VISIBLE : View.GONE;
        int visKopiSusu = (filter.equals("ALL") || filter.equals("COFFEE")) ? View.VISIBLE : View.GONE;
        int visAvocadoCoffee = (filter.equals("ALL") || filter.equals("COFFEE")) ? View.VISIBLE : View.GONE;
        int visGorohoStick = (filter.equals("ALL") || filter.equals("SNACK")) ? View.VISIBLE : View.GONE;
        int visMixPlatter = (filter.equals("ALL") || filter.equals("SNACK")) ? View.VISIBLE : View.GONE;

        if (polaroidCard != null) {
            polaroidCard.setVisibility(visCheesecake);
        }

        View msContainer = findViewById(R.id.mango_sugus_container);
        if (msContainer != null) {
            msContainer.setVisibility(visMangoSugus);
        }

        if (kopiSusuHomeCard != null) {
            kopiSusuHomeCard.setVisibility(visKopiSusu);
        }
        if (avocadoCoffeeHomeCard != null) {
            avocadoCoffeeHomeCard.setVisibility(visAvocadoCoffee);
        }
        if (gorohoStickHomeCard != null) {
            gorohoStickHomeCard.setVisibility(visGorohoStick);
        }
        if (mixPlatterHomeCard != null) {
            mixPlatterHomeCard.setVisibility(visMixPlatter);
        }
    }

    private void setupHeroOnClickListeners() {
        if (heartIconHero != null) {
            heartIconHero.setOnClickListener(v -> Toast.makeText(this, "Added Hero to Favorites", Toast.LENGTH_SHORT).show());
        }
        if (starIconHero != null) {
            starIconHero.setOnClickListener(v -> Toast.makeText(this, "Saved Hero Promotion", Toast.LENGTH_SHORT).show());
        }
    }

    private void setupNavigationClickListeners() {
        btnNavHome.setOnClickListener(v -> selectTab("HOME"));
        btnNavMenu.setOnClickListener(v -> selectTab("FOOD"));
        btnNavCart.setOnClickListener(v -> selectTab("CART"));
        btnNavProfile.setOnClickListener(v -> selectTab("PROFILE"));

        if (btnHeaderCart != null) {
            btnHeaderCart.setOnClickListener(v -> selectTab("CART"));
        }
    }

    private void setupFoodTabOnClickListeners() {
        // Home page cards open detail view or add shortcut
        if (polaroidCard != null) {
            polaroidCard.setOnClickListener(v -> showProductDetail("Basque Cheesecake", 32, R.drawable.bassque_cheesecake, "Basque style burnt cheesecake, creamy and rich."));
        }
        View polAddBtn = findViewById(R.id.pol_add_btn);
        if (polAddBtn != null) {
            polAddBtn.setOnClickListener(v -> {
                addToCart("Basque Cheesecake", 32);
            });
        }
        // Mango Sugus
        if (btnMangoSugusHomeAdd != null) {
            btnMangoSugusHomeAdd.setOnClickListener(v -> {
                addToCart("Mango Sugus", 25);
            });
        }
        if (mangoSugusHomeCard != null) {
            mangoSugusHomeCard.setOnClickListener(v -> showProductDetail("Mango Sugus", 25, R.drawable.mango_sugus, "Zesty creamy nitrogen infused sweet mango classic drink."));
        }
        // Kopi Susu Gula Aren
        if (btnKopiSusuHomeAdd != null) {
            btnKopiSusuHomeAdd.setOnClickListener(v -> {
                addToCart("Kopi Susu Gula Aren", 20);
            });
        }
        if (kopiSusuHomeCard != null) {
            kopiSusuHomeCard.setOnClickListener(v -> showProductDetail("Kopi Susu Gula Aren", 20, R.drawable.kopi_susu_gula_aren, "Sweet traditional palm sugar double shot espresso iced milk."));
        }
        // Avocado Coffee
        if (btnAvocadoCoffeeHomeAdd != null) {
            btnAvocadoCoffeeHomeAdd.setOnClickListener(v -> {
                addToCart("Avocado Coffee", 35);
            });
        }
        if (avocadoCoffeeHomeCard != null) {
            avocadoCoffeeHomeCard.setOnClickListener(v -> showProductDetail("Avocado Coffee", 35, R.drawable.avocado_coffee, "Creamy pure avocado shake mixed with robust espresso shot."));
        }
        // Goroho Stick
        if (btnGorohoStickHomeAdd != null) {
            btnGorohoStickHomeAdd.setOnClickListener(v -> {
                addToCart("Goroho Stick", 20);
            });
        }
        if (gorohoStickHomeCard != null) {
            gorohoStickHomeCard.setOnClickListener(v -> showProductDetail("Goroho Stick", 20, R.drawable.york, "Traditional crispy fried goroho banana stick."));
        }
        // Mix Platter
        if (btnMixPlatterHomeAdd != null) {
            btnMixPlatterHomeAdd.setOnClickListener(v -> {
                addToCart("Mix Platter", 35);
            });
        }
        if (mixPlatterHomeCard != null) {
            mixPlatterHomeCard.setOnClickListener(v -> showProductDetail("Mix Platter", 35, R.drawable.york, "Crispy mix platter with french fries, chicken nuggets, and sausage."));
        }

        // Favorites cards open detail
        if (cardFav1 != null) {
            cardFav1.setOnClickListener(v -> showProductDetail("Kopi Susu Gula Aren", 20, R.drawable.b1, "Sweet traditional palm sugar double shot espresso iced milk."));
        }
        if (btnFavAdd1 != null) {
            btnFavAdd1.setOnClickListener(v -> addToCart("Kopi Susu Gula Aren", 20));
        }

        if (cardFav2 != null) {
            cardFav2.setOnClickListener(v -> showProductDetail("Mango Sugus", 25, R.drawable.mango_sugus, "Zesty creamy nitrogen infused sweet mango classic drink."));
        }
        if (btnFavAdd2 != null) {
            btnFavAdd2.setOnClickListener(v -> addToCart("Mango Sugus", 25));
        }

        if (cardFav3 != null) {
            cardFav3.setOnClickListener(v -> showProductDetail("Avocado Coffee", 35, R.drawable.avocado_coffee, "Creamy pure avocado shake mixed with robust espresso shot."));
        }
        if (btnFavAdd3 != null) {
            btnFavAdd3.setOnClickListener(v -> addToCart("Avocado Coffee", 35));
        }

        // Food main cards open detail
        if (foodMainCard1 != null) {
            foodMainCard1.setOnClickListener(v -> showProductDetail("MBG - Makan Budget Gokil", 18, R.drawable.mbg, "Budget friendly rich chicken rice platter. Fuel for your skate."));
        }
        if (btnFoodMainAdd1 != null) {
            btnFoodMainAdd1.setOnClickListener(v -> addToCart("MBG - Makan Budget Gokil", 18));
        }

        if (foodMainCard2 != null) {
            foodMainCard2.setOnClickListener(v -> showProductDetail("Nasi Cakalang + telur", 20, R.drawable.york, "Traditional spicy cakalang fish mixed with sunny side up egg."));
        }
        if (btnFoodMainAdd2 != null) {
            btnFoodMainAdd2.setOnClickListener(v -> addToCart("Nasi Cakalang + telur", 20));
        }

        if (foodMainCard3 != null) {
            foodMainCard3.setOnClickListener(v -> showProductDetail("Nasi Karage Sambal Matah + telur", 35, R.drawable.york, "Rich fried chicken karage with spicy traditional Balinese sambal matah and sunny side up egg."));
        }
        if (btnFoodMainAdd3 != null) {
            btnFoodMainAdd3.setOnClickListener(v -> addToCart("Nasi Karage Sambal Matah + telur", 35));
        }
    }

    private void setupCartActions() {
        // Empty Cart Refuel Button
        if (btnEmptyCartRefuel != null) {
            btnEmptyCartRefuel.setOnClickListener(v -> selectTab("FOOD"));
        }

        // Order Type Choice: Dine In / Takeaway
        btnTypeDineIn.setOnClickListener(v -> {
            selectedOrderType = "DINE_IN";
            btnTypeDineIn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
            btnTypeDineIn.setTextColor(ContextCompat.getColor(this, R.color.white));
            
            btnTypeTakeaway.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
            btnTypeTakeaway.setTextColor(ContextCompat.getColor(this, R.color.black));
            
            layoutTableInput.setVisibility(View.VISIBLE);
        });

        btnTypeTakeaway.setOnClickListener(v -> {
            selectedOrderType = "TAKEAWAY";
            btnTypeTakeaway.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
            btnTypeTakeaway.setTextColor(ContextCompat.getColor(this, R.color.white));
            
            btnTypeDineIn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
            btnTypeDineIn.setTextColor(ContextCompat.getColor(this, R.color.black));
            
            layoutTableInput.setVisibility(View.GONE);
            inputTableNumber.setText("");
        });

        btnCheckout.setOnClickListener(v -> {
            // Check if user is logged in before checkout
            android.content.SharedPreferences checkPrefs = getSharedPreferences("YorkCafePrefs", MODE_PRIVATE);
            String currentUser = checkPrefs.getString("active_user_name", "Yorkers");
            if (currentUser.equalsIgnoreCase("Yorkers")) {
                Toast.makeText(this, "PLEASE LOGIN FIRST TO CHECKOUT!", Toast.LENGTH_SHORT).show();
                android.content.Intent loginIntent = new android.content.Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                return;
            }

            if (selectedOrderType.isEmpty()) {
                Toast.makeText(this, "PLEASE SELECT DINE IN OR TAKEAWAY FIRST!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedOrderType.equals("DINE_IN")) {
                String tableNo = inputTableNumber.getText().toString().trim();
                if (tableNo.isEmpty()) {
                    Toast.makeText(this, "PLEASE INPUT TABLE NUMBER FOR DINE IN!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            openCheckoutOverlay();
        });
    }

    private void setupDetailOverlayActions() {
        btnDetailBack.setOnClickListener(v -> pageDetail.setVisibility(View.GONE));

        // Sizes
        if (btnSizeS != null) {
            btnSizeS.setOnClickListener(v -> {
                selectedDetailSize = "S";
                btnSizeS.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
                btnSizeS.setTextColor(ContextCompat.getColor(this, R.color.white));
                if (btnSizeM != null) {
                    btnSizeM.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
                    btnSizeM.setTextColor(ContextCompat.getColor(this, R.color.black));
                }
                if (btnSizeL != null) {
                    btnSizeL.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
                    btnSizeL.setTextColor(ContextCompat.getColor(this, R.color.black));
                }
            });
        }

        if (btnSizeM != null) {
            btnSizeM.setOnClickListener(v -> {
                selectedDetailSize = "M";
                btnSizeM.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
                btnSizeM.setTextColor(ContextCompat.getColor(this, R.color.white));
                if (btnSizeS != null) {
                    btnSizeS.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
                    btnSizeS.setTextColor(ContextCompat.getColor(this, R.color.black));
                }
                if (btnSizeL != null) {
                    btnSizeL.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
                    btnSizeL.setTextColor(ContextCompat.getColor(this, R.color.black));
                }
            });
        }

        if (btnSizeL != null) {
            btnSizeL.setOnClickListener(v -> {
                selectedDetailSize = "L";
                btnSizeL.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
                btnSizeL.setTextColor(ContextCompat.getColor(this, R.color.white));
                if (btnSizeS != null) {
                    btnSizeS.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
                    btnSizeS.setTextColor(ContextCompat.getColor(this, R.color.black));
                }
                if (btnSizeM != null) {
                    btnSizeM.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
                    btnSizeM.setTextColor(ContextCompat.getColor(this, R.color.black));
                }
            });
        }

        // Temp
        if (btnTempIced != null) {
            btnTempIced.setOnClickListener(v -> {
                selectedDetailTemp = "ICED";
                btnTempIced.setBackgroundTintList(ColorStateList.valueOf(0xFFDDE1F8));
                btnTempIced.setTextColor(ContextCompat.getColor(this, R.color.black));
                if (btnTempHot != null) {
                    btnTempHot.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
                    btnTempHot.setTextColor(ContextCompat.getColor(this, R.color.black));
                }
            });
        }

        if (btnTempHot != null) {
            btnTempHot.setOnClickListener(v -> {
                selectedDetailTemp = "HOT";
                btnTempHot.setBackgroundTintList(ColorStateList.valueOf(0xFFDDE1F8));
                btnTempHot.setTextColor(ContextCompat.getColor(this, R.color.black));
                if (btnTempIced != null) {
                    btnTempIced.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
                    btnTempIced.setTextColor(ContextCompat.getColor(this, R.color.black));
                }
            });
        }

        if (btnDetailTypeDineIn != null) {
            btnDetailTypeDineIn.setOnClickListener(v -> {
                selectedDetailType = "DINE IN";
                updateTypeSelectionUI("DINE IN");
            });
        }

        if (btnDetailTypeTakeaway != null) {
            btnDetailTypeTakeaway.setOnClickListener(v -> {
                selectedDetailType = "TAKEAWAY";
                updateTypeSelectionUI("TAKEAWAY");
            });
        }

        // Set star clicks
        for (int i = 0; i < 5; i++) {
            final int starIndex = i + 1;
            if (detailStars[i] != null) {
                detailStars[i].setOnClickListener(v -> setDetailProductRating(starIndex));
            }
        }

        // Submit review click
        if (btnSubmitReview != null) {
            btnSubmitReview.setOnClickListener(v -> {
                String comment = inputDetailComment.getText().toString().trim();
                if (currentProductRating == 0) {
                    Toast.makeText(this, "PLEASE SELECT A RATING STAR!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (comment.isEmpty()) {
                    Toast.makeText(this, "PLEASE LEAVE A VIBE COMMENT!", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // Add new review
                productReviewsList.add(0, new ProductReview(detailProductName, currentProductRating, comment, "You"));
                
                // Reset input fields
                currentProductRating = 0;
                if (txtSelectedRating != null) {
                    txtSelectedRating.setText("(0/5)");
                }
                for (int i = 0; i < 5; i++) {
                    if (detailStars[i] != null) {
                        detailStars[i].setImageResource(R.drawable.ic_star_outline);
                        detailStars[i].setImageTintList(ColorStateList.valueOf(0xFF888888));
                    }
                }
                if (inputDetailComment != null) {
                    inputDetailComment.setText("");
                }
                
                // Refresh list
                updateProductReviewsUI(detailProductName);
                Toast.makeText(this, "REVIEW SUBMITTED FOR TESTING!", Toast.LENGTH_SHORT).show();
            });
        }

        btnDetailGrab.setOnClickListener(v -> {
            int finalPrice = detailProductPrice;
            String fullName = detailProductName;

            // Add portion suffix for Indomie items
            if (isIndomieProduct) {
                if (selectedDetailPortion.equals("DOUBLE")) {
                    finalPrice = detailProductPrice + 7;
                    fullName = detailProductName + " Double";
                } else {
                    fullName = detailProductName + " Single";
                }
            }

            if (editingCartItemIndex != -1) {
                if (editingCartItemIndex < cartItems.size()) {
                    CartItem item = cartItems.get(editingCartItemIndex);
                    item.name = fullName;
                    item.unitPrice = finalPrice;
                }
                editingCartItemIndex = -1;
                btnDetailGrab.setText("+ GRAB IT");
                updateCartUI();
                updateCartBadge();
            } else {
                addToCart(fullName, finalPrice);
            }
            pageDetail.setVisibility(View.GONE);
        });

        // Portion button handlers (for Indomie)
        if (btnPortionSingle != null) {
            btnPortionSingle.setOnClickListener(v -> {
                selectedDetailPortion = "SINGLE";
                updatePortionSelectionUI("SINGLE");
                // Reset price to base
                txtDetailPrice.setText(formatPrice(detailProductPrice));
            });
        }
        if (btnPortionDouble != null) {
            btnPortionDouble.setOnClickListener(v -> {
                selectedDetailPortion = "DOUBLE";
                updatePortionSelectionUI("DOUBLE");
                // Show double price (+7K)
                txtDetailPrice.setText(formatPrice(detailProductPrice + 7));
            });
        }
    }

    private void setupCheckoutOverlayActions() {
        btnCheckoutClose.setOnClickListener(v -> pageCheckoutFlow.setVisibility(View.GONE));

        // Payment selections
        btnPayEpay.setOnClickListener(v -> {
            selectedPaymentMethod = "E-PAY";
            btnPayEpay.setCardBackgroundColor(ColorStateList.valueOf(0xFFDDE1F8));
            btnPayQr.setCardBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
        });

        btnPayQr.setOnClickListener(v -> {
            selectedPaymentMethod = "QR SCAN";
            btnPayQr.setCardBackgroundColor(ColorStateList.valueOf(0xFFDDE1F8));
            btnPayEpay.setCardBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
        });

        // Proceed to checkout simulation
        btnCheckoutProceed.setOnClickListener(v -> {
            // Processing payment delay
            Toast.makeText(this, "PROCESSING TRANSACTION...", Toast.LENGTH_SHORT).show();
            
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Random rand = new Random();
                // 75% success rate, 25% crash rate to test both screens!
                boolean success = rand.nextInt(100) < 75;

                pageCheckoutFlow.setVisibility(View.GONE);

                if (success) {
                    currentOrderNumber++;
                    txtSecuredOrderNo.setText("YC-" + (1000 + rand.nextInt(9000)));
                    int total = calculateCartTotal();
                    txtSecuredTotal.setText(formatPrice(total));
                    txtSecuredMethod.setText(selectedOrderType.equals("DINE_IN") ? "DINE IN" : "TAKEAWAY");
                    pageSecured.setVisibility(View.VISIBLE);
                } else {
                    pageCrashed.setVisibility(View.VISIBLE);
                }
            }, 1200);
        });
    }

    private void setupSecuredAndCrashedActions() {
        // Success Secured Actions
        btnSecuredTrack.setOnClickListener(v -> {
            cartItems.clear();
            updateCartUI();
            updateCartBadge();
            pageSecured.setVisibility(View.GONE);
            selectTab("HOME");
            Toast.makeText(this, "DRIVE ON TIME! YOUR ORDER IS PREPARING.", Toast.LENGTH_SHORT).show();
        });

        btnSecuredHome.setOnClickListener(v -> {
            cartItems.clear();
            updateCartUI();
            updateCartBadge();
            pageSecured.setVisibility(View.GONE);
            selectTab("HOME");
        });

        // Crash/Fail Actions
        btnCrashedRetry.setOnClickListener(v -> {
            pageCrashed.setVisibility(View.GONE);
            pageCheckoutFlow.setVisibility(View.VISIBLE);
        });

        btnCrashedAbort.setOnClickListener(v -> {
            cartItems.clear();
            updateCartUI();
            updateCartBadge();
            pageCrashed.setVisibility(View.GONE);
            selectTab("HOME");
        });
    }

    private void setupProfileActions() {
        if (btnPastOrders != null) {
            btnPastOrders.setOnClickListener(v -> Toast.makeText(this, "PAST ORDERS CLICKED!", Toast.LENGTH_SHORT).show());
        }
        if (btnSavedVibes != null) {
            btnSavedVibes.setOnClickListener(v -> Toast.makeText(this, "SAVED VIBES CLICKED!", Toast.LENGTH_SHORT).show());
        }
        if (btnPaymentMethods != null) {
            btnPaymentMethods.setOnClickListener(v -> Toast.makeText(this, "PAYMENT METHODS CLICKED!", Toast.LENGTH_SHORT).show());
        }
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> Toast.makeText(this, "SETTINGS CLICKED!", Toast.LENGTH_SHORT).show());
        }
        if (profileCartBag != null) {
            profileCartBag.setOnClickListener(v -> selectTab("CART"));
        }
        if (btnLogout != null) {
            btnLogout.setPaintFlags(btnLogout.getPaintFlags() | android.graphics.Paint.UNDERLINE_TEXT_FLAG);
            btnLogout.setOnClickListener(v -> {
                android.content.SharedPreferences.Editor editor = getSharedPreferences("YorkCafePrefs", MODE_PRIVATE).edit();
                editor.putString("active_user_name", "Yorkers");
                editor.apply();

                // Update UI immediately using helper
                updateUserSessionUI();

                Toast.makeText(this, "LOGGED OUT SUCCESSFULLY!", Toast.LENGTH_SHORT).show();

                // Redirect to Login screen
                android.content.Intent loginIntent = new android.content.Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            });
        }
    }

    private void showProductDetail(String name, int price, int imageResId, String description) {
        detailProductName = name;
        detailProductPrice = price;
        txtDetailTitle.setText(name.toUpperCase());
        txtDetailDesc.setText(description);
        txtDetailPrice.setText(formatPrice(price));
        imgDetailProduct.setImageResource(imageResId);

        editingCartItemIndex = -1;
        btnDetailGrab.setText("+ GRAB IT");

        selectedDetailSize = "M";
        updateSizeSelectionUI("M");

        selectedDetailTemp = "ICED";
        updateTempSelectionUI("ICED");

        selectedDetailType = "DINE IN";
        updateTypeSelectionUI("DINE IN");

        // Check if this is an Indomie product for PORTION selection
        isIndomieProduct = name.toLowerCase().startsWith("indomie");
        selectedDetailPortion = "SINGLE";
        if (txtPortionLabel != null) {
            txtPortionLabel.setVisibility(isIndomieProduct ? View.VISIBLE : View.GONE);
        }
        if (layoutPortionButtons != null) {
            layoutPortionButtons.setVisibility(isIndomieProduct ? View.VISIBLE : View.GONE);
        }
        if (isIndomieProduct) {
            updatePortionSelectionUI("SINGLE");
        }

        // Reset rating and comment input
        currentProductRating = 0;
        if (txtSelectedRating != null) {
            txtSelectedRating.setText("(0/5)");
        }
        for (int i = 0; i < 5; i++) {
            if (detailStars[i] != null) {
                detailStars[i].setImageResource(R.drawable.ic_star_outline);
                detailStars[i].setImageTintList(ColorStateList.valueOf(0xFF888888));
            }
        }
        if (inputDetailComment != null) {
            inputDetailComment.setText("");
        }

        // Load comments/reviews
        updateProductReviewsUI(name);

        View detailBottomBar = findViewById(R.id.detail_bottom_bar);
        if (detailBottomBar != null) {
            detailBottomBar.setVisibility(View.VISIBLE);
        }

        pageDetail.setVisibility(View.VISIBLE);
    }

    private void setDetailProductRating(int rating) {
        currentProductRating = rating;
        if (txtSelectedRating != null) {
            txtSelectedRating.setText("(" + rating + "/5)");
        }
        for (int i = 0; i < 5; i++) {
            if (detailStars[i] != null) {
                if (i < rating) {
                    detailStars[i].setImageResource(R.drawable.star);
                    detailStars[i].setImageTintList(null);
                } else {
                    detailStars[i].setImageResource(R.drawable.ic_star_outline);
                    detailStars[i].setImageTintList(ColorStateList.valueOf(0xFF888888));
                }
            }
        }
    }

    private void updateProductReviewsUI(String productName) {
        if (layoutReviewsList == null) return;
        layoutReviewsList.removeAllViews();
        boolean hasReviews = false;
        for (ProductReview review : productReviewsList) {
            if (review.productName.equalsIgnoreCase(productName)) {
                hasReviews = true;
                
                com.google.android.material.card.MaterialCardView card = new com.google.android.material.card.MaterialCardView(this);
                card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                card.setRadius(0);
                card.setStrokeColor(ContextCompat.getColor(this, R.color.black));
                card.setStrokeWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.5f, getResources().getDisplayMetrics()));
                
                LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                cardParams.setMargins(0, 0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                card.setLayoutParams(cardParams);

                LinearLayout contentLayout = new LinearLayout(this);
                contentLayout.setOrientation(LinearLayout.VERTICAL);
                int pad = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                contentLayout.setPadding(pad, pad, pad, pad);

                TextView header = new TextView(this);
                StringBuilder stars = new StringBuilder();
                for (int s = 0; s < 5; s++) {
                    if (s < review.rating) stars.append("★");
                    else stars.append("☆");
                }
                header.setText(stars.toString() + "  by " + review.username);
                header.setTextColor(ContextCompat.getColor(this, R.color.black));
                header.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                header.setTypeface(null, android.graphics.Typeface.BOLD);
                contentLayout.addView(header);

                TextView comment = new TextView(this);
                comment.setText(review.comment);
                comment.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
                comment.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                comment.setPadding(0, 4, 0, 0);
                contentLayout.addView(comment);

                card.addView(contentLayout);
                layoutReviewsList.addView(card);
            }
        }

        if (!hasReviews) {
            TextView emptyText = new TextView(this);
            emptyText.setText("NO REVIEWS YET. BE THE FIRST!");
            emptyText.setTextColor(ContextCompat.getColor(this, R.color.grey_light));
            emptyText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            emptyText.setTypeface(null, android.graphics.Typeface.BOLD);
            emptyText.setGravity(android.view.Gravity.CENTER);
            emptyText.setPadding(0, 16, 0, 16);
            layoutReviewsList.addView(emptyText);
        }
    }

    private MenuItem findMenuItemByName(String name) {
        for (MenuItem item : coffeeList) {
            if (item.name.equalsIgnoreCase(name)) return item;
        }
        for (MenuItem item : antiCoffeeList) {
            if (item.name.equalsIgnoreCase(name)) return item;
        }
        for (MenuItem item : spillTeaList) {
            if (item.name.equalsIgnoreCase(name)) return item;
        }
        for (MenuItem item : justCaseList) {
            if (item.name.equalsIgnoreCase(name)) return item;
        }
        for (MenuItem item : milkOptionList) {
            if (item.name.equalsIgnoreCase(name)) return item;
        }
        for (MenuItem item : bitesList) {
            if (item.name.equalsIgnoreCase(name)) return item;
        }
        for (MenuItem item : indomieList) {
            if (item.name.equalsIgnoreCase(name)) return item;
        }
        for (MenuItem item : moreList) {
            if (item.name.equalsIgnoreCase(name)) return item;
        }

        if (name.equalsIgnoreCase("Basque Cheesecake")) {
            return new MenuItem("Basque Cheesecake", 32, "Basque style burnt cheesecake, creamy and rich.", R.drawable.york);
        }
        if (name.equalsIgnoreCase("Oat Latte")) {
            return new MenuItem("Oat Latte", 6, "Velvety oat milk coffee option. Pure smooth drift.", R.drawable.b2);
        }
        if (name.equalsIgnoreCase("Kopi Susu Gula Aren")) {
            return new MenuItem("Kopi Susu Gula Aren", 20, "Sweet traditional palm sugar double shot espresso iced milk.", R.drawable.kopi_susu_gula_aren);
        }
        if (name.equalsIgnoreCase("Mango Sugus")) {
            return new MenuItem("Mango Sugus", 25, "Zesty creamy nitrogen infused sweet mango classic drink.", R.drawable.mango_sugus);
        }
        if (name.equalsIgnoreCase("Avocado Coffee")) {
            return new MenuItem("Avocado Coffee", 35, "Creamy pure avocado shake mixed with robust espresso shot.", R.drawable.avocado_coffee);
        }
        if (name.equalsIgnoreCase("MBG - Makan Budget Gokil")) {
            return new MenuItem("MBG - Makan Budget Gokil", 18, "Budget friendly rich chicken rice platter. Fuel for your skate.", R.drawable.b1);
        }
        if (name.equalsIgnoreCase("Nasi Cakalang + telur")) {
            return new MenuItem("Nasi Cakalang + telur", 20, "Traditional spicy cakalang fish mixed with sunny side up egg.", R.drawable.york);
        }
        if (name.equalsIgnoreCase("Nasi Karage Sambal Matah + telur")) {
            return new MenuItem("Nasi Karage Sambal Matah + telur", 35, "Rich fried chicken karage with Balinese sambal matah and sunny side up egg.", R.drawable.york);
        }
        return null;
    }

    private void showProductDetailForEdit(int cartIndex) {
        editingCartItemIndex = cartIndex;
        CartItem cartItem = cartItems.get(cartIndex);

        String fullName = cartItem.name;
        String baseName = fullName;
        String size = "M";
        String temp = "ICED";
        String type = "DINE IN";

        if (fullName.contains("(")) {
            int openParen = fullName.indexOf("(");
            int closeParen = fullName.indexOf(")");
            if (openParen != -1 && closeParen != -1 && closeParen > openParen) {
                baseName = fullName.substring(0, openParen).trim();
                String optionsStr = fullName.substring(openParen + 1, closeParen);
                if (optionsStr.contains(",")) {
                    String[] parts = optionsStr.split(",");
                    if (parts.length >= 1) size = parts[0].trim();
                    if (parts.length >= 2) temp = parts[1].trim();
                    if (parts.length >= 3) type = parts[2].trim();
                } else {
                    type = optionsStr.trim();
                }
            }
        }

        MenuItem menuItem = findMenuItemByName(baseName);
        if (menuItem == null) {
            menuItem = new MenuItem(baseName, cartItem.unitPrice, "Custom selected item.", R.drawable.york);
        }

        showProductDetail(menuItem.name, menuItem.price, menuItem.imageResId, menuItem.desc);

        editingCartItemIndex = cartIndex;
        btnDetailGrab.setText("UPDATE STASH");

        selectedDetailSize = size;
        updateSizeSelectionUI(size);

        selectedDetailTemp = temp;
        updateTempSelectionUI(temp);

        selectedDetailType = type;
        updateTypeSelectionUI(type);
    }

    private void updateSizeSelectionUI(String size) {
        if (btnSizeS != null) {
            btnSizeS.setBackgroundTintList(ColorStateList.valueOf(size.equals("S") ? ContextCompat.getColor(this, R.color.black) : ContextCompat.getColor(this, R.color.white)));
            btnSizeS.setTextColor(size.equals("S") ? ContextCompat.getColor(this, R.color.white) : ContextCompat.getColor(this, R.color.black));
        }

        if (btnSizeM != null) {
            btnSizeM.setBackgroundTintList(ColorStateList.valueOf(size.equals("M") ? ContextCompat.getColor(this, R.color.black) : ContextCompat.getColor(this, R.color.white)));
            btnSizeM.setTextColor(size.equals("M") ? ContextCompat.getColor(this, R.color.white) : ContextCompat.getColor(this, R.color.black));
        }

        if (btnSizeL != null) {
            btnSizeL.setBackgroundTintList(ColorStateList.valueOf(size.equals("L") ? ContextCompat.getColor(this, R.color.black) : ContextCompat.getColor(this, R.color.white)));
            btnSizeL.setTextColor(size.equals("L") ? ContextCompat.getColor(this, R.color.white) : ContextCompat.getColor(this, R.color.black));
        }
    }

    private void updateTempSelectionUI(String temp) {
        if (btnTempIced != null) {
            btnTempIced.setBackgroundTintList(ColorStateList.valueOf(temp.equals("ICED") ? 0xFFDDE1F8 : ContextCompat.getColor(this, R.color.white)));
            btnTempIced.setTextColor(ContextCompat.getColor(this, R.color.black));
        }

        if (btnTempHot != null) {
            btnTempHot.setBackgroundTintList(ColorStateList.valueOf(temp.equals("HOT") ? 0xFFDDE1F8 : ContextCompat.getColor(this, R.color.white)));
            btnTempHot.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
    }

    private void updateTypeSelectionUI(String type) {
        if (btnDetailTypeDineIn != null) {
            btnDetailTypeDineIn.setBackgroundTintList(ColorStateList.valueOf(type.equals("DINE IN") ? 0xFFDDE1F8 : ContextCompat.getColor(this, R.color.white)));
            btnDetailTypeDineIn.setTextColor(ContextCompat.getColor(this, R.color.black));
        }

        if (btnDetailTypeTakeaway != null) {
            btnDetailTypeTakeaway.setBackgroundTintList(ColorStateList.valueOf(type.equals("TAKEAWAY") ? 0xFFDDE1F8 : ContextCompat.getColor(this, R.color.white)));
            btnDetailTypeTakeaway.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
    }

    private void updatePortionSelectionUI(String portion) {
        if (btnPortionSingle != null) {
            if (portion.equals("SINGLE")) {
                btnPortionSingle.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
                btnPortionSingle.setTextColor(ContextCompat.getColor(this, R.color.white));
            } else {
                btnPortionSingle.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
                btnPortionSingle.setTextColor(ContextCompat.getColor(this, R.color.black));
            }
        }
        if (btnPortionDouble != null) {
            if (portion.equals("DOUBLE")) {
                btnPortionDouble.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
                btnPortionDouble.setTextColor(ContextCompat.getColor(this, R.color.white));
            } else {
                btnPortionDouble.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
                btnPortionDouble.setTextColor(ContextCompat.getColor(this, R.color.black));
            }
        }
    }

    private void openCheckoutOverlay() {
        checkoutItemsContainer.removeAllViews();

        int total = 0;
        Random rand = new Random();
        txtCheckoutReceiptOrderNo.setText("ORDER #" + (100 + rand.nextInt(900)));

        for (CartItem item : cartItems) {
            total += item.unitPrice * item.quantity;

            LinearLayout itemRow = new LinearLayout(this);
            itemRow.setOrientation(LinearLayout.HORIZONTAL);
            itemRow.setPadding(0, 4, 0, 4);

            TextView leftText = new TextView(this);
            leftText.setText(item.quantity + "x " + item.name.toUpperCase());
            leftText.setTextColor(ContextCompat.getColor(this, R.color.black));
            leftText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            leftText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));

            TextView rightText = new TextView(this);
            rightText.setText(formatPrice(item.unitPrice * item.quantity));
            rightText.setTextColor(ContextCompat.getColor(this, R.color.black));
            rightText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            itemRow.addView(leftText);
            itemRow.addView(rightText);
            checkoutItemsContainer.addView(itemRow);
        }

        txtCheckoutSubtotal.setText(formatPrice(total));
        txtCheckoutGiantTotal.setText(formatPrice(total));

        if (selectedOrderType.equals("DINE_IN")) {
            String tableNo = inputTableNumber.getText().toString().trim();
            txtCheckoutMethod.setText("DINE IN (TABLE " + tableNo + ")");
        } else {
            txtCheckoutMethod.setText("TAKEAWAY");
        }

        // Set default payment method E-PAY
        selectedPaymentMethod = "E-PAY";
        btnPayEpay.setCardBackgroundColor(ColorStateList.valueOf(0xFFDDE1F8));
        btnPayQr.setCardBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));

        pageCheckoutFlow.setVisibility(View.VISIBLE);
    }

    private int calculateCartTotal() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.unitPrice * item.quantity;
        }
        return total;
    }

    private void selectTab(String tab) {
        selectTab(tab, true);
    }

    private void selectTab(String tab, boolean addToHistory) {
        if (mainViewPager == null) return;

        int position = 0;
        switch (tab) {
            case "HOME":
                position = 0;
                break;
            case "FOOD":
                position = 1;
                break;
            case "CART":
                position = 2;
                break;
            case "PROFILE":
                position = 3;
                break;
        }

        if (mainViewPager.getCurrentItem() != position) {
            mainViewPager.setCurrentItem(position, false);
        } else {
            updateNavUIForPosition(position);
        }

        // Add to history
        if (addToHistory) {
            tabHistory.remove(tab);
            tabHistory.add(tab);
        }

        Log.d(TAG, "Selected tab: " + tab + ", history size: " + tabHistory.size());
    }

    private void updateNavUIForPosition(int position) {
        imgNavHome.setBackground(null);
        imgNavMenu.setBackground(null);
        imgNavCart.setBackground(null);
        imgNavProfile.setBackground(null);

        switch (position) {
            case 0:
                imgNavHome.setBackgroundResource(R.drawable.bg_active_nav);
                break;
            case 1:
                imgNavMenu.setBackgroundResource(R.drawable.bg_active_nav);
                break;
            case 2:
                imgNavCart.setBackgroundResource(R.drawable.bg_active_nav);
                updateCartUI();
                break;
            case 3:
                imgNavProfile.setBackgroundResource(R.drawable.bg_active_nav);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (pageDetail.getVisibility() == View.VISIBLE) {
            pageDetail.setVisibility(View.GONE);
        } else if (pageCheckoutFlow.getVisibility() == View.VISIBLE) {
            pageCheckoutFlow.setVisibility(View.GONE);
        } else if (pageSecured.getVisibility() == View.VISIBLE) {
            pageSecured.setVisibility(View.GONE);
        } else if (pageCrashed.getVisibility() == View.VISIBLE) {
            pageCrashed.setVisibility(View.GONE);
        } else {
            // If we are currently on the Home page (main menu), exit directly.
            if (mainViewPager != null && mainViewPager.getCurrentItem() == 0) {
                showExitConfirmationDialog();
            } else {
                // Otherwise, navigate back to the Home page
                selectTab("HOME");
            }
        }
    }

    private void showExitConfirmationDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("EXIT APP");
        builder.setMessage("Are you sure you want to exit York Cafe?");
        builder.setPositiveButton("YES", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("NO", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

        try {
            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
        } catch (Exception e) {
            // fallback
        }
    }

    private void addToCart(String name, int price) {
        boolean found = false;
        for (CartItem item : cartItems) {
            if (item.name.equalsIgnoreCase(name)) {
                item.quantity++;
                found = true;
                break;
            }
        }
        if (!found) {
            cartItems.add(new CartItem(name, price, 1));
        }
        Toast.makeText(this, name + " added to stash!", Toast.LENGTH_SHORT).show();
        updateCartBadge();
        updateCartUI();
    }

    private void updateCartBadge() {
        if (txtCartBadge == null) return;
        int totalQty = 0;
        for (CartItem item : cartItems) {
            totalQty += item.quantity;
        }
        if (totalQty > 0) {
            txtCartBadge.setText(String.valueOf(totalQty));
            txtCartBadge.setVisibility(View.VISIBLE);
        } else {
            txtCartBadge.setVisibility(View.GONE);
        }
    }

    private String formatPrice(int price) {
        if (price < 10) {
            return "$" + price;
        } else {
            return price + "K";
        }
    }

    private void updateCartUI() {
        if (cartItemsList == null || layoutEmptyCart == null || layoutActiveCart == null || txtCartTotal == null) {
            return;
        }

        cartItemsList.removeAllViews();

        if (cartItems.isEmpty()) {
            layoutEmptyCart.setVisibility(View.VISIBLE);
            layoutActiveCart.setVisibility(View.GONE);
            return;
        }

        layoutEmptyCart.setVisibility(View.GONE);
        layoutActiveCart.setVisibility(View.VISIBLE);

        int total = 0;

        for (int i = 0; i < cartItems.size(); i++) {
            final int index = i;
            final CartItem item = cartItems.get(i);
            total += item.unitPrice * item.quantity;

            com.google.android.material.card.MaterialCardView card = new com.google.android.material.card.MaterialCardView(this);
            card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
            card.setRadius(0);
            card.setStrokeColor(ContextCompat.getColor(this, R.color.black));
            card.setStrokeWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2.5f, getResources().getDisplayMetrics()));
            
            card.setRotation(0);

            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));
            card.setLayoutParams(cardParams);

            LinearLayout horizontalLayout = new LinearLayout(this);
            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
            horizontalLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
            horizontalLayout.setPadding(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics())
            );

            LinearLayout textLayout = new LinearLayout(this);
            textLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            textLayout.setLayoutParams(textLayoutParams);

            TextView itemTitle = new TextView(this);
            itemTitle.setText(item.name.toUpperCase());
            itemTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
            itemTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            try {
                itemTitle.setTypeface(ResourcesCompat.getFont(this, R.font.anton));
            } catch (Exception e) {
                itemTitle.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
            }
            textLayout.addView(itemTitle);

            TextView itemPrice = new TextView(this);
            itemPrice.setText(formatPrice(item.unitPrice) + " each");
            itemPrice.setTextColor(ContextCompat.getColor(this, R.color.grey_dark));
            itemPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            itemPrice.setPadding(0, 4, 0, 0);
            textLayout.addView(itemPrice);

            horizontalLayout.addView(textLayout);

            LinearLayout countLayout = new LinearLayout(this);
            countLayout.setOrientation(LinearLayout.HORIZONTAL);
            countLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);

            MaterialButton btnMinus = new MaterialButton(this, null, com.google.android.material.R.attr.materialButtonStyle);
            btnMinus.setText("-");
            btnMinus.setTextColor(ContextCompat.getColor(this, R.color.black));
            btnMinus.setStrokeColor(ContextCompat.getColorStateList(this, R.color.black));
            btnMinus.setStrokeWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.5f, getResources().getDisplayMetrics()));
            btnMinus.setCornerRadius(0);
            btnMinus.setInsetBottom(0);
            btnMinus.setInsetTop(0);
            btnMinus.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, getResources().getDisplayMetrics())
            );
            btnMinus.setLayoutParams(btnParams);
            btnMinus.setPadding(0, 0, 0, 0);
            btnMinus.setOnClickListener(v -> {
                if (item.quantity > 1) {
                    item.quantity--;
                } else {
                    cartItems.remove(index);
                }
                updateCartUI();
                updateCartBadge();
            });
            countLayout.addView(btnMinus);

            TextView qtyText = new TextView(this);
            qtyText.setText(String.valueOf(item.quantity));
            qtyText.setTextColor(ContextCompat.getColor(this, R.color.black));
            qtyText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            qtyText.setTypeface(null, android.graphics.Typeface.BOLD);
            qtyText.setPadding(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()),
                    0,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()),
                    0
            );
            countLayout.addView(qtyText);

            MaterialButton btnPlus = new MaterialButton(this, null, com.google.android.material.R.attr.materialButtonStyle);
            btnPlus.setText("+");
            btnPlus.setTextColor(ContextCompat.getColor(this, R.color.black));
            btnPlus.setStrokeColor(ContextCompat.getColorStateList(this, R.color.black));
            btnPlus.setStrokeWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.5f, getResources().getDisplayMetrics()));
            btnPlus.setCornerRadius(0);
            btnPlus.setInsetBottom(0);
            btnPlus.setInsetTop(0);
            btnPlus.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            btnPlus.setLayoutParams(btnParams);
            btnPlus.setPadding(0, 0, 0, 0);
            btnPlus.setOnClickListener(v -> {
                item.quantity++;
                updateCartUI();
                updateCartBadge();
            });
            countLayout.addView(btnPlus);

            textLayout.setOnClickListener(v -> showProductDetailForEdit(index));
            card.setOnClickListener(v -> showProductDetailForEdit(index));

            horizontalLayout.addView(countLayout);
            card.addView(horizontalLayout);
            cartItemsList.addView(card);
        }

        txtCartTotal.setText(formatPrice(total));
    }

    private void populateMenuCategory(LinearLayout container, List<MenuItem> items) {
        if (container == null) return;
        container.removeAllViews();

        for (final MenuItem item : items) {
            com.google.android.material.card.MaterialCardView card = new com.google.android.material.card.MaterialCardView(this);
            card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
            card.setRadius(0);
            card.setStrokeColor(ContextCompat.getColor(this, R.color.black));
            card.setStrokeWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2.0f, getResources().getDisplayMetrics()));

            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics())
            );
            cardParams.setMargins(0, 0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
            card.setLayoutParams(cardParams);

            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
            int pad = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
            rowLayout.setPadding(pad, 0, pad, 0);

            // Title
            TextView title = new TextView(this);
            title.setText(item.name);
            title.setTextColor(ContextCompat.getColor(this, R.color.black));
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            title.setTypeface(null, android.graphics.Typeface.BOLD);
            
            LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            title.setLayoutParams(titleParams);
            rowLayout.addView(title);

            // Card click listener opens Detail Page!
            card.setOnClickListener(v -> showProductDetail(item.name, item.price, item.imageResId, item.desc));

            // Price Sticker
            TextView priceTag = new TextView(this);
            priceTag.setText(item.price + "K");
            priceTag.setTextColor(ContextCompat.getColor(this, R.color.white));
            priceTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            priceTag.setTypeface(null, android.graphics.Typeface.BOLD);
            priceTag.setBackgroundResource(R.drawable.bg_black_sticker);
            priceTag.setPadding(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics())
            );
            LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            priceParams.setMargins(0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics()), 0);
            priceTag.setLayoutParams(priceParams);
            rowLayout.addView(priceTag);

            // Plus Button (shortcut to directly add to cart)
            MaterialButton btnAdd = new MaterialButton(this, null, com.google.android.material.R.attr.materialButtonStyle);
            btnAdd.setText("+");
            btnAdd.setTextColor(ContextCompat.getColor(this, R.color.white));
            btnAdd.setStrokeColor(ContextCompat.getColorStateList(this, R.color.black));
            btnAdd.setStrokeWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.5f, getResources().getDisplayMetrics()));
            btnAdd.setCornerRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, getResources().getDisplayMetrics()));
            btnAdd.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
            btnAdd.setInsetBottom(0);
            btnAdd.setInsetTop(0);
            btnAdd.setPadding(0, 0, 0, 0);

            int btnSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(btnSize, btnSize);
            btnAdd.setLayoutParams(btnParams);
            btnAdd.setOnClickListener(v -> {
                addToCart(item.name, item.price);
            });
            rowLayout.addView(btnAdd);

            card.addView(rowLayout);
            container.addView(card);
        }
    }

    private void populateBitesGrid(LinearLayout container, List<MenuItem> items) {
        if (container == null) return;
        container.removeAllViews();

        for (int i = 0; i < items.size(); i += 2) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            rowParams.setMargins(0, 0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics()));
            row.setLayoutParams(rowParams);

            final MenuItem item1 = items.get(i);
            row.addView(createBiteCard(item1, i));

            if (i + 1 < items.size()) {
                final MenuItem item2 = items.get(i + 1);
                row.addView(createBiteCard(item2, i + 1));
            } else {
                View dummy = new View(this);
                LinearLayout.LayoutParams dummyParams = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.0f
                );
                dummyParams.setMargins((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics()), 0, 0, 0);
                dummy.setLayoutParams(dummyParams);
                row.addView(dummy);
            }

            container.addView(row);
        }
    }

    private View createBiteCard(final MenuItem item, int index) {
        com.google.android.material.card.MaterialCardView card = new com.google.android.material.card.MaterialCardView(this);
        card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
        card.setRadius(0);
        card.setStrokeColor(ContextCompat.getColor(this, R.color.black));
        card.setStrokeWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2.0f, getResources().getDisplayMetrics()));

        float rotation = (index % 2 == 0) ? -1.5f : 1.5f;
        card.setRotation(rotation);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());
        if (index % 2 == 0) {
            cardParams.setMargins(0, 0, margin, 0);
        } else {
            cardParams.setMargins(margin, 0, 0, 0);
        }
        card.setLayoutParams(cardParams);

        // Click listener on Card opens Detail
        card.setOnClickListener(v -> showProductDetail(item.name, item.price, item.imageResId, item.desc));

        LinearLayout cardContent = new LinearLayout(this);
        cardContent.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());
        cardContent.setPadding(pad, pad, pad, pad);

        TextView title = new TextView(this);
        title.setText(item.name);
        title.setTextColor(ContextCompat.getColor(this, R.color.black));
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        title.setMinLines(2);
        cardContent.addView(title);

        LinearLayout bottomRow = new LinearLayout(this);
        bottomRow.setOrientation(LinearLayout.HORIZONTAL);
        bottomRow.setGravity(android.view.Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams bottomParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        bottomParams.setMargins(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()), 0, 0);
        bottomRow.setLayoutParams(bottomParams);

        TextView priceTag = new TextView(this);
        priceTag.setText(item.price + "K");
        priceTag.setTextColor(ContextCompat.getColor(this, R.color.white));
        priceTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        priceTag.setTypeface(null, android.graphics.Typeface.BOLD);
        priceTag.setBackgroundResource(R.drawable.bg_black_sticker);
        priceTag.setPadding(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics())
            );
        LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        priceTag.setLayoutParams(priceParams);
        bottomRow.addView(priceTag);

        MaterialButton btnAdd = new MaterialButton(this, null, com.google.android.material.R.attr.materialButtonStyle);
        btnAdd.setText("+");
        btnAdd.setTextColor(ContextCompat.getColor(this, R.color.white));
        btnAdd.setStrokeColor(ContextCompat.getColorStateList(this, R.color.black));
        btnAdd.setStrokeWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.0f, getResources().getDisplayMetrics()));
        btnAdd.setCornerRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics()));
        btnAdd.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
        btnAdd.setInsetBottom(0);
        btnAdd.setInsetTop(0);
        btnAdd.setPadding(0, 0, 0, 0);

        int btnSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(btnSize, btnSize);
        btnAdd.setLayoutParams(btnParams);
        btnAdd.setOnClickListener(v -> {
            addToCart(item.name, item.price);
        });
        bottomRow.addView(btnAdd);

        cardContent.addView(bottomRow);
        card.addView(cardContent);
        return card;
    }

    private void populateStraightGrid(LinearLayout container, List<MenuItem> items) {
        if (container == null) return;
        container.removeAllViews();

        for (int i = 0; i < items.size(); i += 2) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            rowParams.setMargins(0, 0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics()));
            row.setLayoutParams(rowParams);

            final MenuItem item1 = items.get(i);
            row.addView(createStraightBiteCard(item1, i));

            if (i + 1 < items.size()) {
                final MenuItem item2 = items.get(i + 1);
                row.addView(createStraightBiteCard(item2, i + 1));
            } else {
                View dummy = new View(this);
                LinearLayout.LayoutParams dummyParams = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.0f
                );
                dummyParams.setMargins((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics()), 0, 0, 0);
                dummy.setLayoutParams(dummyParams);
                row.addView(dummy);
            }

            container.addView(row);
        }
    }

    private View createStraightBiteCard(final MenuItem item, int index) {
        com.google.android.material.card.MaterialCardView card = new com.google.android.material.card.MaterialCardView(this);
        card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
        card.setRadius(0);
        card.setStrokeColor(ContextCompat.getColor(this, R.color.black));
        card.setStrokeWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2.0f, getResources().getDisplayMetrics()));

        // No rotation - perfectly straight layout

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());
        if (index % 2 == 0) {
            cardParams.setMargins(0, 0, margin, 0);
        } else {
            cardParams.setMargins(margin, 0, 0, 0);
        }
        card.setLayoutParams(cardParams);

        // Click listener on Card opens Detail
        card.setOnClickListener(v -> showProductDetail(item.name, item.price, item.imageResId, item.desc));

        LinearLayout cardContent = new LinearLayout(this);
        cardContent.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());
        cardContent.setPadding(pad, pad, pad, pad);

        TextView title = new TextView(this);
        title.setText(item.name);
        title.setTextColor(ContextCompat.getColor(this, R.color.black));
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        title.setMinLines(2);
        cardContent.addView(title);

        LinearLayout bottomRow = new LinearLayout(this);
        bottomRow.setOrientation(LinearLayout.HORIZONTAL);
        bottomRow.setGravity(android.view.Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams bottomParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        bottomParams.setMargins(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()), 0, 0);
        bottomRow.setLayoutParams(bottomParams);

        TextView priceTag = new TextView(this);
        priceTag.setText(item.price + "K");
        priceTag.setTextColor(ContextCompat.getColor(this, R.color.white));
        priceTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        priceTag.setTypeface(null, android.graphics.Typeface.BOLD);
        priceTag.setBackgroundResource(R.drawable.bg_black_sticker);
        priceTag.setPadding(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics())
            );
        LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        priceTag.setLayoutParams(priceParams);
        bottomRow.addView(priceTag);

        MaterialButton btnAdd = new MaterialButton(this, null, com.google.android.material.R.attr.materialButtonStyle);
        btnAdd.setText("+");
        btnAdd.setTextColor(ContextCompat.getColor(this, R.color.white));
        btnAdd.setStrokeColor(ContextCompat.getColorStateList(this, R.color.black));
        btnAdd.setStrokeWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.0f, getResources().getDisplayMetrics()));
        btnAdd.setCornerRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics()));
        btnAdd.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
        btnAdd.setInsetBottom(0);
        btnAdd.setInsetTop(0);
        btnAdd.setPadding(0, 0, 0, 0);

        int btnSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(btnSize, btnSize);
        btnAdd.setLayoutParams(btnParams);
        btnAdd.setOnClickListener(v -> {
            addToCart(item.name, item.price);
        });
        bottomRow.addView(btnAdd);

        cardContent.addView(bottomRow);
        card.addView(cardContent);
        return card;
    }

    private void applyGrayscaleFilter(ImageView imageView) {
        if (imageView == null) return;
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(filter);
    }

    private static class ViewPagerAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {
        private final List<View> mViews;

        ViewPagerAdapter(List<View> views) {
            this.mViews = views;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
            FrameLayout container = new FrameLayout(parent.getContext());
            container.setLayoutParams(new android.view.ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT));
            return new ViewHolder(container);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            FrameLayout container = (FrameLayout) holder.itemView;
            View view = mViews.get(position);
            
            // Remove view from its current parent if any
            if (view.getParent() != null) {
                ((android.view.ViewGroup) view.getParent()).removeView(view);
            }
            
            // Force layout params to match parent inside ViewPager2
            view.setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT));
            
            container.removeAllViews();
            container.addView(view);
        }

        @Override
        public int getItemCount() {
            return mViews.size();
        }

        static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}