package com.sap.core.odata.core.uri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.uri.ExpandSelectTreeNode;
import com.sap.core.odata.api.uri.NavigationPropertySegment;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.core.uri.ExpandSelectTreeNodeImpl.AllKinds;

public class ExpandSelectTreeCreator {

  private List<SelectItem> initialSelect;
  private List<ArrayList<NavigationPropertySegment>> initialExpand;

  public ExpandSelectTreeCreator(final List<SelectItem> select, final List<ArrayList<NavigationPropertySegment>> expand) {
    if (select != null) {
      initialSelect = select;
    } else {
      initialSelect = Collections.emptyList();
    }

    if (expand != null) {
      initialExpand = expand;
    } else {
      initialExpand = Collections.emptyList();
    }
  }

  public ExpandSelectTreeNode create() throws EdmException {

    //Initial node
    ExpandSelectTreeNodeImpl root = new ExpandSelectTreeNodeImpl();
    if (!initialSelect.isEmpty()) {
      //Create a full expand tree
      createSelectTree(root);
    } else {
      //If no select is given the root node is explicitly selected for all expand clauses
      root.setExplicitlySeleced();
    }
    //Merge in the expand tree
    mergeExpandTree(root);

    //consolidate the tree
    consolidate(root);
    return root;
  }

  private void consolidate(final ExpandSelectTreeNodeImpl node) {
    switch (node.getAllKind()) {
    case EXPLICITLYTRUE:
    case IMPLICITLYTRUE:
      consolidateTrueNode(node);
      break;
    case FALSE:
      consolidateFalseNode(node);
    }

  }

  private void consolidateFalseNode(final ExpandSelectTreeNodeImpl node) {
    Iterator<Map.Entry<String, ExpandSelectTreeNode>> iterator = node.getLinks().entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<String, ExpandSelectTreeNode> entry = iterator.next();
      ExpandSelectTreeNodeImpl subNode = (ExpandSelectTreeNodeImpl) entry.getValue();
      if (!subNode.isExpanded()) {
        entry.setValue(null);
      } else {
        consolidate(subNode);
      }
    }
  }

  private void consolidateTrueNode(final ExpandSelectTreeNodeImpl node) {
    Iterator<Map.Entry<String, ExpandSelectTreeNode>> iterator = node.getLinks().entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<String, ExpandSelectTreeNode> entry = iterator.next();
      ExpandSelectTreeNodeImpl subNode = (ExpandSelectTreeNodeImpl) entry.getValue();
      if (subNode.isExpanded() && node.isExplicitlySeleced()) {
        subNode.setExplicitlySeleced();
        consolidate(subNode);
      } else if (subNode.isExpanded()) {
        consolidate(subNode);
      } else {
        iterator.remove();
      }
    }
  }

  private void createSelectTree(final ExpandSelectTreeNodeImpl root) throws EdmException {
    for (SelectItem item : initialSelect) {
      ExpandSelectTreeNodeImpl actualNode = root;

      for (NavigationPropertySegment navSegement : item.getNavigationPropertySegments()) {
        actualNode = addSelectNode(actualNode, navSegement.getNavigationProperty().getName());
      }

      if (item.getProperty() != null) {
        actualNode.addProperty(item.getProperty());
      } else if (item.isStar()) {
        actualNode.setAllExplicitly();
      } else {
        //The actual node is a navigation property and has no property or star so it is explicitly selected
        actualNode.setExplicitlySeleced();
      }
    }
  }

  private ExpandSelectTreeNodeImpl addSelectNode(final ExpandSelectTreeNodeImpl actualNode, final String navigationPropertyName) {
    Map<String, ExpandSelectTreeNode> links = actualNode.getLinks();
    if (!links.containsKey(navigationPropertyName)) {
      ExpandSelectTreeNodeImpl subNode = new ExpandSelectTreeNodeImpl();
      links.put(navigationPropertyName, subNode);
      if (actualNode.isExplicitlySeleced()) {
        //if a node was explicitly selected all sub nodes are explicitly selected
        subNode.setExplicitlySeleced();
      } else {
        if (actualNode.getAllKind() == AllKinds.IMPLICITLYTRUE) {
          actualNode.setAllKindFalse();
        }
      }
      return subNode;
    } else {
      return (ExpandSelectTreeNodeImpl) links.get(navigationPropertyName);
    }
  }

  private void mergeExpandTree(final ExpandSelectTreeNodeImpl root) throws EdmException {
    for (ArrayList<NavigationPropertySegment> singleExpand : initialExpand) {
      ExpandSelectTreeNodeImpl actualNode = root;
      for (NavigationPropertySegment navSegement : singleExpand) {
        actualNode = addExpandNode(actualNode, navSegement.getNavigationProperty().getName());
        if (actualNode == null) {
          break;
        }
      }
    }

  }

  private ExpandSelectTreeNodeImpl addExpandNode(final ExpandSelectTreeNodeImpl actualNode, final String navigationPropertyName) {
    Map<String, ExpandSelectTreeNode> links = actualNode.getLinks();
    if (!links.containsKey(navigationPropertyName)) {
      if (actualNode.isExplicitlySeleced() || (actualNode.isExplicitlySeleced() && actualNode.isExpanded())) {
        ExpandSelectTreeNodeImpl subNode = new ExpandSelectTreeNodeImpl();
        subNode.setExpanded();
        subNode.setExplicitlySeleced();
        links.put(navigationPropertyName, subNode);
        return subNode;
      } else {
        return null;
      }
    } else {
      ExpandSelectTreeNodeImpl subNode = (ExpandSelectTreeNodeImpl) links.get(navigationPropertyName);
      subNode.setExpanded();
      return subNode;
    }
  }

}