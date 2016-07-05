package heritage.config;

public final class MenuItem
{
    private final String menuItem;
    private final boolean editable;

    public MenuItem( String menuItem, boolean editable )
    {
        this.menuItem = menuItem;
        this.editable = editable;
    }

    public String getMenuItem()
    {
        return menuItem;
    }

    public boolean isEditable()
    {
        return editable;
    }
}