package sigma.app.api.object.signature;

public class SignatureTotalPrices {

	private String monthlyTotal;
	private String yearlyTotal;
	private String totalSpent;
	
	public SignatureTotalPrices() {}

	public String getMonthlyTotal() {
		return monthlyTotal;
	}
	public void setMonthlyTotal(String monthlyTotal) {
		this.monthlyTotal = monthlyTotal;
	}
	public String getYearlyTotal() {
		return yearlyTotal;
	}
	public void setYearlyTotal(String yearlyTotal) {
		this.yearlyTotal = yearlyTotal;
	}

	public String getTotalSpent() {
		return totalSpent;
	}

	public void setTotalSpent(String totalSpent) {
		this.totalSpent = totalSpent;
	}
	
	
}
