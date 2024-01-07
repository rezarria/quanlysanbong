package io.rezarria.projection;

import io.rezarria.model.Bill;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Projection for {@link io.rezarria.model.Bill}
 */
public interface BillInfo {
    Instant getLastModifiedDate();

    UUID getId();

    Bill.PaymentMethod getPaymentMethod();

    Bill.PaymentStatus getPaymentStatus();

    double getTotalPrice();

    String getDescription();

    CustomerInfo getCustomer();

    List<BillDetailInfo> getDetails();

    FieldHistoryInfo getFieldHistory();

    OrganizationInfo getOrganization();

    /**
     * Projection for {@link io.rezarria.model.Customer}
     */
    interface CustomerInfo {
        UUID getId();

        String getName();

        String getAvatar();
    }

    /**
     * Projection for {@link io.rezarria.model.BillDetail}
     */
    interface BillDetailInfo {
        Instant getLastModifiedDate();

        UUID getId();

        long getCount();

        ProductInfo getProduct();

        ProductPriceInfo getPrice();

        /**
         * Projection for {@link io.rezarria.model.Product}
         */
        interface ProductInfo {
            Instant getLastModifiedDate();

            UUID getId();

            String getName();

            String getDescription();
        }

        /**
         * Projection for {@link io.rezarria.model.ProductPrice}
         */
        interface ProductPriceInfo {
            UUID getId();

            double getPrice();

            String getDescription();
        }
    }

    /**
     * Projection for {@link io.rezarria.model.FieldHistory}
     */
    interface FieldHistoryInfo {
        UUID getId();

        int getUnitSize();

        Instant getFrom();

        Instant getTo();

        FieldInfo getField();

        StaffInfo getStaff();

        /**
         * Projection for {@link io.rezarria.model.Field}
         */
        interface FieldInfo {
            Instant getLastModifiedDate();

            UUID getId();

            String getName();

            String getDescription();

            boolean isActive();

            Set<ProductImageInfo> getImages();

            /**
             * Projection for {@link io.rezarria.model.ProductImage}
             */
            interface ProductImageInfo {
                UUID getId();

                String getPath();
            }
        }

        /**
         * Projection for {@link io.rezarria.model.Staff}
         */
        interface StaffInfo {
            Instant getLastModifiedDate();

            UUID getId();

            String getName();

            String getAvatar();
        }
    }

    /**
     * Projection for {@link io.rezarria.model.Organization}
     */
    interface OrganizationInfo {
        UUID getId();

        String getName();

        String getImage();
    }
}