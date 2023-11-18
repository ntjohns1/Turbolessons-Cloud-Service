package com.noslen.paymentservice.service;

import com.noslen.paymentservice.dto.ProductDto;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Product;
import com.stripe.model.StripeCollection;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.ProductUpdateParams;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService {

    private final StripeClient stripeClient;
    private final StripeClientHelper stripeClientHelper;

    public ProductServiceImpl(StripeClient stripeClient, StripeClientHelper stripeClientHelper) {
        this.stripeClient = stripeClient;
        this.stripeClientHelper = stripeClientHelper;
    }

    @Override
    public Mono<StripeCollection<Product>> listAllProducts() {
//        return Mono.fromCallable(() -> stripeClient.products()
//                        .list())
//                .onErrorMap(StripeException.class,
//                            e -> new Exception("Error processing Stripe API",
//                                               e));
        return stripeClientHelper.executeStripeCall(() -> stripeClient.products()
                .list());
    }

    @Override
    public Mono<Product> retrieveProduct(String id) {
        return stripeClientHelper.executeStripeCall(() -> stripeClient.products()
                .retrieve(id));
    }

    @Override
    public Mono<Product> createProduct(ProductDto productDto) {
        ProductCreateParams params = ProductCreateParams.builder()
                .setName(productDto.getName())
                .setDescription(productDto.getDescription())
                .build();
//        return Mono.fromCallable(() -> stripeClient.products()
//                        .create(params))
//                .onErrorMap(StripeException.class,
//                            e -> new Exception("Error processing Stripe API",
//                                               e));
        return stripeClientHelper.executeStripeCall(() -> stripeClient.products()
                .create(params));
    }

    @Override
    public Mono<Void> updateProduct(String id, ProductDto productDto) {
        ProductUpdateParams params = ProductUpdateParams.builder()
                .setName(productDto.getName())
                .setDescription(productDto.getDescription())
                .build();
//        return Mono.fromRunnable(() -> {
//                    try {
//                        stripeClient.products()
//                                .update(id);
//                    } catch (StripeException e) {
//                        throw new RuntimeException(e);
//                    }
//                })
//                .onErrorMap(ex -> {
//                    if (ex.getCause() instanceof StripeException) {
//                        return new Exception("Error processing Stripe API",
//                                             ex.getCause());
//                    }
//                    return ex;
//                })
//                .then();
        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.products()
                .update(id));
    }

    @Override
    public Mono<Void> deleteProduct(String id) {
//        return Mono.fromRunnable(() -> {
//                    try {
//                        stripeClient.products()
//                                .delete(id);
//                    } catch (StripeException e) {
//                        throw new RuntimeException(e);
//                    }
//                })
//                .onErrorMap(ex -> {
//                    if (ex.getCause() instanceof StripeException) {
//                        return new Exception("Error processing Stripe API",
//                                             ex.getCause());
//                    }
//                    return ex;
//                })
//                .then();
    return stripeClientHelper.executeStripeVoidCall(()-> stripeClient.products()
            .delete(id));
    }

}
