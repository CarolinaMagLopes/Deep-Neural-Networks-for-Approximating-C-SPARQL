package eu.larkc.csparql.sr4ld2014;

import eu.larkc.csparql.common.RDFTable;
import eu.larkc.csparql.common.RDFTuple;
import eu.larkc.csparql.core.ResultFormatter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Observable;

public class ConsoleFormatter2 extends ResultFormatter {

    private FileOutputStream file;
    public ConsoleFormatter2(FileOutputStream file) {
        this.file = file;
    }

    public void update(Observable o, Object arg) {

        RDFTable q = (RDFTable)arg;
        System.out.println();
        System.out.println("-------" + q.size() + " results at SystemTime=[" + System.currentTimeMillis() + "]--------");
        Iterator<RDFTuple> var4 = q.iterator();

        if(var4.hasNext()) {
            while (var4.hasNext()) {
                RDFTuple t = (RDFTuple) var4.next();
                System.out.println(t.toString());
                try {
                    file.write(("\n" + t.toString()).getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                file.write("\n\n\n".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        System.out.println();
    }
}
