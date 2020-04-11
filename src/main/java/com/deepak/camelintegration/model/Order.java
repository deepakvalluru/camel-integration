package com.deepak.camelintegration.model;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order implements Serializable
{

   private static final long serialVersionUID = 1L;

   private String            orderNumber;
   private String            productId;
   private double            amount;
   private String            priority;
   }
