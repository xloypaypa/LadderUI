package net;

import java.io.File;

/**
 * Created by xlo on 15-7-14.
 * it's return folder list.
 */
public class ListFolderSolver extends ShowFolderStoppableAllDownloadServer {
    @Override
    protected void buildPage() {
        page = "<h2>\r\n";
        page += this.file.getPath() + "\r\n";
        page += "</h2>\r\n";
        page += "<a href=\"" + getPath(this.file.getParent()) + "\">" + "parent folder</a><br><br>\r\n";
        String extra;
        File[] kids = this.file.listFiles();
        if (kids != null) {
            for (File now : kids) {
                if (now.isFile()) {
                    extra = " (file)";
                } else if (now.isDirectory()) {
                    extra = " (folder)";
                } else {
                    extra = " (unknown)";
                }
                page += "<a href=\"" + getPath(now.getPath()) + "\">" + now.getPath() + extra + "</a><br>\r\n";
            }
        }
    }
}
