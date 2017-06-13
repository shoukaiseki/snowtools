package com.shoukaiseki.maximo.anngoukafukugou;

import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.Properties;

public class PropertiesUtil
{
  public static String CRYPTOX_ALGORITHM = "DESede";
  public static String CRYPTOX_MODE = "CBC";
  public static String CRYPTOX_PADDING = "PKCS5Padding";
  public static String CRYPTOX_KEY = "j3*9vk0e8rjvc9fj(*KFikd#";
  public static String CRYPTOX_SPEC = "kE*(RKc%";
  public static String CRYPTOX_MODULUS = "";
  public static String CRYPTOX_PROVIDER = null;
  private String algorithm;
  private String mode;
  private String padding;
  private String key;
  private String spec;
  private String modulus;
  private String provider;

  public PropertiesUtil()
  {
    Properties prop = new Properties();
    FileInputStream fis = null;
    try {
      fis = new FileInputStream("./conf.properties");
      prop.load(fis);

      this.algorithm = prop.getProperty("mxe.security.cryptox.algorithm", CRYPTOX_ALGORITHM);
      this.mode = prop.getProperty("mxe.security.cryptox.mode", CRYPTOX_MODE);
      this.padding = prop.getProperty("mxe.security.cryptox.padding", CRYPTOX_PADDING);
      this.key = prop.getProperty("mxe.security.cryptox.key", CRYPTOX_KEY);
      this.spec = prop.getProperty("mxe.security.cryptox.spec", CRYPTOX_SPEC);
      this.modulus = prop.getProperty("mxe.security.cryptox.modulus", CRYPTOX_MODULUS);
      this.provider = prop.getProperty("mxe.security.provider", CRYPTOX_PROVIDER);
      fis.close();
    } catch (Exception e) {
      System.out.println(e.getMessage() + "配置文件(./conf.properties)读取有误。");
    }
  }

  public void setAlgorithm(String algorithm)
  {
    this.algorithm = algorithm;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public void setPadding(String padding) {
    this.padding = padding;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public void setSpec(String spec) {
    this.spec = spec;
  }

  public void setModulus(String modulus) {
    this.modulus = modulus;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }

  public String getAlgorithm() {
    return this.algorithm;
  }

  public String getMode() {
    return this.mode;
  }

  public String getPadding() {
    return this.padding;
  }

  public String getKey() {
    return this.key;
  }

  public String getSpec() {
    return this.spec;
  }

  public String getModulus() {
    return this.modulus;
  }

  public String getProvider() {
    return this.provider;
  }
}