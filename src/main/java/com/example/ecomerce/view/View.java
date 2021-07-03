package com.example.ecomerce.view;
/**
 * This interface defines different Views for different models in application.
 *
 * @author Orlando Kuan
 *
 */
public interface View {
  public static interface UserView {
    //Request View for User
    public static interface Request {
    }
    //Response View for User, will inherit all filds in Request
    public static interface Response extends Request {
    }
  }
}
