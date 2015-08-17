package net;

import server.serverSolver.normalServer.AllDownloadServerSolver;

import java.io.IOException;

/**
 * Created by xlo on 15-7-9.
 * it's the file server can be stoppable.
 */
public class StoppableAllDownloadServer extends AllDownloadServerSolver {
    @Override
    protected boolean sendingHead() {
        this.requestSolver.getReplyHeadWriter().setMessage("OK");
        this.requestSolver.getReplyHeadWriter().setVersion("HTTP/1.1");
        this.requestSolver.getReplyHeadWriter().addMessage("Content-Disposition", "attachment;filename=" + this.file.getName());

        if (this.requestSolver.getRequestHeadReader().getMessage("Range") == null) {
            this.requestSolver.getReplyHeadWriter().setReply(200);
            this.requestSolver.getReplyHeadWriter().addMessage("Content-Length", "" + this.file.length());
        } else {
            this.requestSolver.getReplyHeadWriter().setReply(206);
            String range = this.requestSolver.getRequestHeadReader().getMessage("Range");
            if (!solveRangeMessage(range)) return false;
        }
        return this.requestSolver.getReplyHeadWriter().sendHead();
    }

    protected boolean solveRangeMessage(String range) {
        int equalPos = range.indexOf('=');
        int halfPos = range.indexOf('-');
        long start, end;
        start = Long.valueOf(range.substring(equalPos + 1, halfPos));
        if (halfPos == range.length() - 1) {
            end = this.file.length() - 1;
        } else {
            end = Long.valueOf(range.substring(halfPos + 1));
        }
        String ans = "bytes " + start + "-" + end + "/" + this.file.length();
        try {
            long skip = this.fileIOBuilder.getInputStream().skip(start);
            if (skip != start) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        buildReplyRangeMessage(start, end, ans);
        return true;
    }

    private void buildReplyRangeMessage(long start, long end, String ans) {
        this.requestSolver.getReplyHeadWriter().addMessage("Content-Range", ans);
        this.requestSolver.getReplyHeadWriter().addMessage("Content-Length", (end - start + 1) + "");
    }
}
