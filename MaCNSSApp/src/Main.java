import service.MaCNSSService;
import dao.RefundFileDAOImpl;
public class Main {

    public static void main(String[] args) {
        try {

            MaCNSSService MaCNSSService = service.MaCNSSService.getInstance();
            MaCNSSService.start();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}