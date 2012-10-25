package com.sap.core.odata.api.edm;

/**
 * EdmType holds the namespace of a given type an its type as {@link EdmTypeKind}.
 * 
 * Do not implement this interface. This interface is intended for usage only.
 * 
 * @author SAP AG
 */
public interface EdmType extends EdmNamed {

  /**
   * Get the namespace of the type
   * 
   * @return namespace as String
   * @throws EdmException
   */
  String getNamespace() throws EdmException;;

  /**
   * Get the type kind
   * 
   * @return {@link EdmTypeKind}
   */
  EdmTypeKind getKind();
}