package com.nutrisport.shared

import nutrisport.shared.generated.resources.Res
import nutrisport.shared.generated.resources.back_arrow
import nutrisport.shared.generated.resources.book
import nutrisport.shared.generated.resources.cat
import nutrisport.shared.generated.resources.check
import nutrisport.shared.generated.resources.checkmark_image
import nutrisport.shared.generated.resources.close
import nutrisport.shared.generated.resources.delete
import nutrisport.shared.generated.resources.dollar
import nutrisport.shared.generated.resources.edit
import nutrisport.shared.generated.resources.google_logo
import nutrisport.shared.generated.resources.grid
import nutrisport.shared.generated.resources.home
import nutrisport.shared.generated.resources.india
import nutrisport.shared.generated.resources.log_in
import nutrisport.shared.generated.resources.log_out
import nutrisport.shared.generated.resources.map_pin
import nutrisport.shared.generated.resources.menu
import nutrisport.shared.generated.resources.minus
import nutrisport.shared.generated.resources.paypal_logo
import nutrisport.shared.generated.resources.plus
import nutrisport.shared.generated.resources.right_arrow
import nutrisport.shared.generated.resources.search
import nutrisport.shared.generated.resources.serbia
import nutrisport.shared.generated.resources.shopping_cart
import nutrisport.shared.generated.resources.shopping_cart_image
import nutrisport.shared.generated.resources.unlock
import nutrisport.shared.generated.resources.usa
import nutrisport.shared.generated.resources.user
import nutrisport.shared.generated.resources.vertical_menu
import nutrisport.shared.generated.resources.warning
import nutrisport.shared.generated.resources.weight

/**
 * Centralized access point for application drawable resources.
 *
 * This object organizes icons and images used across the UI layer
 * to provide a consistent and maintainable way of referencing assets.
 *
 * Grouping resources in a structured hierarchy avoids hardcoding
 * drawable references throughout the codebase and improves readability.
 *
 * The resources are sourced from Compose Multiplatform's generated
 * `Res` class and can be used across supported platforms.
 */
object Resources {

    /**
     * Collection of commonly used UI icons.
     *
     * These icons are typically used for interactive elements such as
     * buttons, navigation items, and action indicators.
     */
    object Icon {

        /** Icon representing an addition action. */
        val Plus = Res.drawable.plus

        /** Icon representing a subtraction or removal action. */
        val Minus = Res.drawable.minus

        /** Icon used for sign-in actions. */
        val SignIn = Res.drawable.log_in

        /** Icon used for sign-out actions. */
        val SignOut = Res.drawable.log_out

        /** Icon representing unlocking or authentication success. */
        val Unlock = Res.drawable.unlock

        /** Icon used for search functionality. */
        val Search = Res.drawable.search

        /** Icon representing a user or profile. */
        val Person = Res.drawable.user

        /** Icon indicating success or confirmation. */
        val Checkmark = Res.drawable.check

        /** Icon used for edit actions. */
        val Edit = Res.drawable.edit

        /** Icon representing a navigation menu or drawer. */
        val Menu = Res.drawable.menu

        /** Icon used for navigating back. */
        val BackArrow = Res.drawable.back_arrow

        /** Icon indicating forward navigation. */
        val RightArrow = Res.drawable.right_arrow

        /** Icon representing the home screen. */
        val Home = Res.drawable.home

        /** Icon representing a shopping cart. */
        val ShoppingCart = Res.drawable.shopping_cart

        /** Icon used to represent product categories. */
        val Categories = Res.drawable.grid

        /** Icon representing currency or price. */
        val Dollar = Res.drawable.dollar

        /** Icon representing a location or address. */
        val MapPin = Res.drawable.map_pin

        /** Icon used to close dialogs or screens. */
        val Close = Res.drawable.close

        /** Icon representing reading or documentation. */
        val Book = Res.drawable.book

        /** Icon used for overflow menus or additional options. */
        val VerticalMenu = Res.drawable.vertical_menu

        /** Icon representing delete or removal actions. */
        val Delete = Res.drawable.delete

        /** Icon used for warning or alert messages. */
        val Warning = Res.drawable.warning

        /** Icon representing weight or measurement. */
        val Weight = Res.drawable.weight
    }

    /**
     * Collection of image assets used within the application UI.
     *
     * These images are typically used for illustrations,
     * branding, or decorative UI elements.
     */
    object Image {

        /** Illustration used in shopping cart related screens. */
        val ShoppingCart = Res.drawable.shopping_cart_image

        /** Illustration used to represent successful actions. */
        val Checkmark = Res.drawable.checkmark_image

        /** Decorative image used in UI layouts. */
        val Cat = Res.drawable.cat

        /** Official Google logo used for authentication flows. */
        val GoogleLogo = Res.drawable.google_logo

        /** PayPal logo used for payment related screens. */
        val PaypalLogo = Res.drawable.paypal_logo
    }

    object Flag {
        val India = Res.drawable.india
        val Usa = Res.drawable.usa
        val Serbia = Res.drawable.serbia
    }
}