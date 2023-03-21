package sigma.app.api.model.signature;

import java.util.Comparator;

import sigma.app.api.object.signature.SignatureDTO;


public class SignatureDateComparator implements Comparator<SignatureDTO> {

	@Override
	public int compare(SignatureDTO signature01, SignatureDTO signature02) {
		if (signature01.getNextPaymentDate() == null && signature02.getNextPaymentDate() == null) {
			 return 0;
		} else if (signature01.getNextPaymentDate() != null && signature02.getNextPaymentDate() == null) {
			 return 1;
		} else if (signature01.getNextPaymentDate() == null && signature02.getNextPaymentDate() != null) {
			 return -1;
		} else if (signature01.getNextPaymentDate().before(signature02.getNextPaymentDate())) {
            return -1;
        } else if (signature01.getNextPaymentDate().after(signature02.getNextPaymentDate())) {
            return 1;
        } else {
            return 0;
        }   
	}
}
