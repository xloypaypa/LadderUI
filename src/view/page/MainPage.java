package view.page;

import view.main.UIPage;
import view.page.kidPage.TabbedPageKidPage;

import javax.swing.*;
import java.util.Vector;

/**
 * Created by xlo on 15-6-21.
 * it's main page.
 */
public class MainPage extends NamedPage implements UIPage {
    JTabbedPane show;
    Vector<TabbedPageKidPage> pages;

    public MainPage() {
        super("main");
        show=new JTabbedPane(JTabbedPane.TOP);
        show.setName("main");
        show.setBounds(0, 0, 300, 600);
        show.setBackground(null);
        pages=new Vector<>();
    }

    public void addPage(TabbedPageKidPage page){
        pages.addElement(page);
    }

    public void cleanPage(){
        pages.removeAllElements();
    }

    @Override
    public void getInstance() {
        for (TabbedPageKidPage page : pages) {
            page.getInstance();
            show.add(page.getName(), page.getPage());
        }
        show.setSelectedIndex(0);
    }

    @Override
    public void repaint() {
        int nowPage=show.getSelectedIndex();

        for (TabbedPageKidPage page : pages) {
            show.remove(page.getPage());
        }
        pages.forEach(view.page.kidPage.TabbedPageKidPage::repaint);
        for (TabbedPageKidPage page : pages) {
            show.add(page.getName(), page.getPage());
        }

        show.setSelectedIndex(nowPage);
        show.repaint();
    }

    @Override
    public JComponent getPage() {
        return show;
    }

    @Override
    public boolean isThisName(String s) {
        return this.isThisPage(s);
    }

    @Override
    public String getClassName() {
        return this.getName();
    }
}
