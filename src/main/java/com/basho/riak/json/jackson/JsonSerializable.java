package com.basho.riak.json.jackson;

/**
 * Used to distinguish between types that serialize
 * and those which do not.
 * 
 * Implementing classes must ensure that at least one
 * getter is present, and the getter function must conform
 * to the java bean naming standard of 'getXYZ'.
 * 
 * If properties can not be located on a implementing type,
 * the serialization concern will throw a {@link RJSerializationError}.
 * 
 * @author Randy Secrist
 */
public interface JsonSerializable {

}
