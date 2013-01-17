package com.sap.core.odata.core;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import com.sap.core.odata.api.uri.PathInfo;
import com.sap.core.odata.api.uri.PathSegment;

public class PathInfoImpl implements PathInfo {

  private List<PathSegment> precedingPathSegment = Collections.emptyList();
  private List<PathSegment> odataPathSegment = Collections.emptyList();
  private URI serviceRoot;

  public void setODataPathSegment(List<PathSegment> odataPathSegement) {
    this.odataPathSegment = odataPathSegement;
  }

  public void setPrecedingPathSegment(List<PathSegment> precedingPathSegement) {
    this.precedingPathSegment = precedingPathSegement;
  }

  public void setServiceRoot(URI uri) {
    this.serviceRoot = uri;
  }

  @Override
  public List<PathSegment> getPrecedingSegments() {
    return Collections.unmodifiableList(this.precedingPathSegment);
  }

  @Override
  public List<PathSegment> getODataSegments() {
    return Collections.unmodifiableList(this.odataPathSegment);
  }

  @Override
  public URI getServiceRoot() {
    return this.serviceRoot;
  }
}