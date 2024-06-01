package com.semester.tinder.controller;

import com.semester.tinder.config.ConfigVnPay;
import com.semester.tinder.dto.VnPay.PaymentRestDTO;
import com.semester.tinder.dto.VnPay.TransactionStatusDTO;
import com.semester.tinder.dto.request.RabbitMQ.EmailNotify;
import com.semester.tinder.dto.response.ApiResponse;
import com.semester.tinder.entity.HistoryOrder;
import com.semester.tinder.entity.TinderVip;
import com.semester.tinder.entity.User;
import com.semester.tinder.entity.VipUsers;
import com.semester.tinder.repository.IHistoryOrderRepo;
import com.semester.tinder.repository.ITinderVipRepo;
import com.semester.tinder.repository.IUserRepo;
import com.semester.tinder.repository.IVipUsersRepo;
import com.semester.tinder.service.RappidMQ.RabbitMQService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;


@RestController
@RequestMapping("/public/payment")
public class PaymentController {

    @Autowired
    private RabbitMQService rabbitMQService;

    @Autowired
    private ITinderVipRepo _iTinderVipRepo;
    @Autowired
    private IUserRepo _iUserRepo;
    @Autowired
    private IHistoryOrderRepo _iHistoryOrderRepo;

    @Autowired
    private IVipUsersRepo _iVipUsersRepo;

    // lấy ra 2 gói tinder đang có:
    @GetMapping("/get-all/tinder/packages")
    public ApiResponse<List<TinderVip>> getAll(){
        ApiResponse<List<TinderVip>> tinder_v = new ApiResponse<>();

        tinder_v.setMessage("get successfully");
        tinder_v.setCode(200);
        tinder_v.setResult( _iTinderVipRepo.findAll() );

        return tinder_v;
    }


    @GetMapping("/create-payment")
    public ResponseEntity<?> createPayment(HttpServletRequest req, @RequestParam int code, @RequestParam int idUser) throws UnsupportedEncodingException {

//        String orderType = "other";
//        long amount = Integer.parseInt(req.getParameter("amount"))*100;
//        String bankCode = req.getParameter("bankCode");


        // check xem ta co su dung goi nay chua?
        Optional<VipUsers> vu = _iVipUsersRepo.findIsExist(idUser);
// chi cho phep nang cap goi con lai null
        if(vu.isPresent()){
            if(vu.get().getId() == code) return ResponseEntity.status(HttpStatus.OK).body("sorry you are registered");
            if( vu.get().getId() == 2 ) return ResponseEntity.status(HttpStatus.OK).body("the package you use is the most advanced");
        }

        Optional<TinderVip> pac = _iTinderVipRepo.findById(code);
        if(pac.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found package");


        long amount = (long) (pac.get().getPrice() * 100); // nhan voi 100 de ra gia tien goc!

        String vnp_TxnRef = ConfigVnPay.getRandomNumber(8);
        String vnp_TmnCode = ConfigVnPay.vnp_TmnCode;
        String orderType = "other";
        String Payment1 = String.valueOf(code) + " " + String.valueOf(idUser);

        String vnp_IpAddr = ConfigVnPay.getIpAddress(req);

        System.out.println("Thanh toan don hang:" + String.valueOf(idUser));

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", Payment1); //vnp_TxnRef noi dung thanh toan don hang + ma
        vnp_Params.put("vnp_OrderType", orderType);

        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", ConfigVnPay.vnp_ReturnUrl); // + "?contractId=" + contractId lay tren param truy vao
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
     //   vnp_Params.put("cus_UserId", String.valueOf(idUser));
        //vnp_Params.put("vnp_UserId", String.valueOf(idUser)); error!

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = ConfigVnPay.hmacSHA512(ConfigVnPay.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = ConfigVnPay.vnp_PayUrl + "?" + queryUrl;

       return ResponseEntity.status(HttpStatus.OK).body(paymentUrl);

    }

// dữ liệu mình cần nó sẽ ở trên cái url tra về o method tren
    @GetMapping("/payment_res")
    public ResponseEntity<?> transaction(
            @RequestParam( value = "vnp_Amount") String amount,
            @RequestParam( value = "vnp_BankCode") String bankCode,
            @RequestParam( value = "vnp_CardType") String cardType,
            @RequestParam( value = "vnp_OrderInfo") String order,
            @RequestParam( value = "vnp_ResponseCode") String responseCode,
            @RequestParam( value = "vnp_PayDate") String payDate,
            @RequestParam( value = "vnp_TmnCode") String tmnCode
    ){

        TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();

//        String[] new_a = bankCode.split(":");
//        int new_s = Integer.parseInt(new_a[1].trim());

        // neu thanh toan thanh cong thi chung ta chay vao if
        if( responseCode.equals("00") ){


            String[] new_a = order.split(" ");

            int code_tinder_package = Integer.parseInt( new_a[0].trim() );
            int idUser = Integer.parseInt(new_a[1].trim());
            Optional<TinderVip> pac = _iTinderVipRepo.findById(code_tinder_package);

            double new_amount = Double.parseDouble(amount) / 100;

            Date parseDate = new Date();

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                parseDate = dateFormat.parse(payDate);

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            // neu ma da co trong database roi
            Optional<VipUsers> vu1 = _iVipUsersRepo.findIsExist(idUser);
            Optional<User> u =  _iUserRepo.findById(idUser);
            if(vu1.isPresent() && pac.orElseThrow().getId() == 2){
                vu1.get().setStart_time(LocalDateTime.now());
                vu1.get().setTinderVip( pac.orElseThrow() );
                vu1.get().setEnd_time( LocalDateTime.now().plusMonths(1) );
                transactionStatusDTO.setData("Congrats you registered platinum tinder done! ");
            } else if ( vu1.isPresent() && pac.orElseThrow().getId() == 1 ) {
                vu1.get().setEnd_time( LocalDateTime.now().plusWeeks(1) );
                transactionStatusDTO.setData("Congrats you registered plus tinder done! ");
            } else{
                // luu order vao lich su thanh toan

                // lưu vao bảng userVip
                VipUsers vu = new VipUsers();
                vu.setUser(u.orElseThrow());
                vu.setStart_time(LocalDateTime.now());
                vu.setTinderVip( pac.orElseThrow() );
                if(code_tinder_package == 1){
                    // vu.setEnd_time( LocalDateTime.now().plusMonths(1) );
                    vu.setEnd_time( LocalDateTime.now().plusWeeks(1) );
                    transactionStatusDTO.setData("Congrats you registered plus tinder done! ");
                }else {
                    vu.setEnd_time( LocalDateTime.now().plusMonths(1) );
                    transactionStatusDTO.setData("Congrats you registered platinum tinder done!  ");
                }

                _iVipUsersRepo.save(vu);
            }


            // lưu vao bảng historyOrder
            HistoryOrder historyOrder = new HistoryOrder();
            historyOrder.setUser( u.orElseThrow() );
            historyOrder.setVnp_amount(new_amount);
            historyOrder.setVnp_pay_date(parseDate);
            historyOrder.setVnp_card_type(cardType);
            historyOrder.setVnp_bank_code(bankCode);
            historyOrder.setVnp_tmn_code(tmnCode);

            _iHistoryOrderRepo.save(historyOrder);
            // return ve du lieu thanh toan thanh cong:
            transactionStatusDTO.setStatus(200);
            transactionStatusDTO.setMessage("Transaction successfully");

            // GỬU ORDER VỀ NODE JS DE GUU TIN NHAN:
            EmailNotify emailNotify = new EmailNotify();
            emailNotify.setOrderId(tmnCode);
            emailNotify.setEmail( u.orElseThrow().getEmail() );
            emailNotify.setAmount( new_amount );
            emailNotify.setDatePayment( parseDate );
            emailNotify.setBankName(bankCode);
            emailNotify.setPackageName( pac.get().getName() );
            emailNotify.setCodeNotify(3);
            emailNotify.setNameUser( u.orElseThrow().getFullname() );
            emailNotify.setPhone( u.orElseThrow().getPhone_number() );

            // gửu email cho client
            rabbitMQService.sendMessage("node_channel", emailNotify);

        }else {

            // neu ko thanh toan thanh cong guu thong bao that bai
            transactionStatusDTO.setStatus(400);
            transactionStatusDTO.setMessage("Transaction fail!");
            transactionStatusDTO.setData("Sorry has some problems make for transaction is canceled please try later!");

        }

        return ResponseEntity.status(HttpStatus.OK).body( transactionStatusDTO );
    }

}
