package com.basho.riak.json;

/**
 *
 * @author Randy Secrist
 */
public class Version {
  public static String getVersion() {
    String pkg_ver = Version.class.getPackage().getSpecificationVersion();
    return (pkg_ver != null) ? pkg_ver : "DEVELOPMENT";
  }
}
