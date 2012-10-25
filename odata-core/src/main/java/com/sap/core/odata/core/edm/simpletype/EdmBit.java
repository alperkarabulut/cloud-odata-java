package com.sap.core.odata.core.edm.simpletype;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmTypeKind;

public class EdmBit implements EdmSimpleType {

  @Override
  public boolean equals(Object obj) {
    boolean equals = false;
    if (this == obj) {
      equals = true;
    } else if (obj instanceof EdmBit) {
      equals = true;
    }

    return equals;
  }

  @Override
  public String getNamespace() throws EdmException {
    return EdmSimpleTypeFacade.systemNamespace;
  }

  @Override
  public EdmTypeKind getKind() {
    return EdmTypeKind.SIMPLE;
  }

  @Override
  public String getName() throws EdmException {
    return "Bit";
  }

  @Override
  public boolean isCompatible(EdmSimpleType simpleType) {
    boolean compatible;

    if (simpleType instanceof EdmBit) {
      compatible = true;
    } else {
      compatible = false;
    }

    return compatible;
  }

  @Override
  public boolean validate(String value, EdmLiteralKind literalKind, EdmFacets facets) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Object valueOfString(String value, EdmLiteralKind literalKind, EdmFacets facets) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String valueToString(Object value, EdmLiteralKind literalKind, EdmFacets facets) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String toUriLiteral(String literal) {
    // TODO Auto-generated method stub
    return null;
  }
}
