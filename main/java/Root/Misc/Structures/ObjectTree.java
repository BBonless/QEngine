package Root.Misc.Structures;

import Root.Objects.WorldObject;

import java.util.ArrayList;

public class ObjectTree {

    public ObjectTree Parent;
    public WorldObject Element;
    public ArrayList<ObjectTree> Children = new ArrayList<>();

    public void AddChild(ObjectTree NewNode) {
        NewNode.Parent = this;
        Children.add(NewNode);
    }

    public void AddChild(WorldObject Object) {
        ObjectTree NewNode = new ObjectTree(Object);
        NewNode.Parent = this;
        Children.add(NewNode);
    }

    public void RemoveChild(ObjectTree Node) {
        Node.Parent = null;
        Children.remove(Node);
    }

    public boolean HasChildren() {
        return !Children.isEmpty();
    }

    public boolean ValidateName(String Name) {
        if (Element.Name.equals(Name)) {
            return false;
        }

        for (ObjectTree ChildNode : Children) {
            if (ChildNode.ValidateName(Name) == false) {
                return false;
            }
        }

        return true;
    }

    public ObjectTree() {}
    public ObjectTree(WorldObject Element) {this.Element = Element;}

}
