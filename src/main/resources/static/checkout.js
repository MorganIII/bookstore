// add your public key here
const stripe = Stripe("pk_test_**");

// fill the order request with the below data and send it to the backend to create the order
const orderRequest = {
    shippingAddress:{
    fullName: "mosaab morgan",
    mobileNumber: "",
    government: "",
    city: "",
    fullAddress: ""
    },
    paymentMethod:"CARD"
};

let elements;

const options = {
    mode: 'payment',
    amount: 11099,
    currency: 'egp',
    appearance:{
        theme: 'night',
        labels: 'floating',
    }
}


elements = stripe.elements(options);

  const paymentElement = elements.create('payment');
  paymentElement.mount('#payment-element');


const form = document.getElementById('payment-form');
const submitBtn = document.getElementById('submit');

const handleError = (error) => {
  const messageContainer = document.querySelector('#error-message');
  messageContainer.textContent = error.message;
  submitBtn.disabled = false;
}

form.addEventListener('submit', async (event) => {
  // We don't want to let default form submission happen here,
  // which would refresh the page.
  event.preventDefault();

  // Prevent multiple form submissions
  if (submitBtn.disabled) {
    return;
  }

  // Disable form submission while loading
  submitBtn.disabled = true;

  // Trigger form validation and wallet collection
  const {error: submitError} = await elements.submit();
  if (submitError) {
    handleError(submitError);
    return;
  }

  // Create the PaymentIntent and obtain clientSecret
  const res = await fetch("http://localhost:8080/api/orders", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtb3JnYW5AZ21haWwuY29tIiwiaWF0IjoxNzA4ODkyNDkxLCJleHAiOjE3MDg5Nzg4OTF9.Yl5Rs42covVXANtazIGnYT0tyaiOk6-sdnKEZk2iHvo"
    },
    body: JSON.stringify(orderRequest),
  });

  const {client_secret: clientSecret} = await res.json();

  // Confirm the PaymentIntent using the details collected by the Payment Element
  const {error} = await stripe.confirmPayment({
    elements,
    clientSecret,
    confirmParams: {
      return_url: 'https://example.com/order/123/complete',
    },
  });

  if (error) {
    // This point is only reached if there's an immediate error when
    // confirming the payment. Show the error to your customer (for example, payment details incomplete)
    handleError(error);
  } else {
    // Your customer is redirected to your `return_url`. For some payment
    // methods like iDEAL, your customer is redirected to an intermediate
    // site first to authorize the payment, then redirected to the `return_url`.
  }
});
