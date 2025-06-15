'use client'

import { useState } from "react"; 

export default function LoginPage() {

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const loginUser = async () => {
    const response = await fetch("http://localhost:8080/users/login", {
      method: "POST",
      headers: { "Content-Type": "text/plain" },
      body: `${email}\n${password}`
    });

    if (response.ok) {
      alert("login success!");
    } else {
      alert(await response.text());
    }
  }

  return (
    <>
      <h1 className="text-gray-700 text-3xl">Login Page</h1>
      <div className="flex flex-col">
        <form action={loginUser}>
          <div className="flex flex-col">
            <label htmlFor="email">Email</label>
            <input id="email" type="email" className="border w-64" onChange={(e) => setEmail(e.target.value)} required/>
          </div>
          <div className="flex flex-col">
            <label htmlFor="password">Password</label>
            <input id="password" type="password" className="border w-64" onChange={(e) => setPassword(e.target.value)} required/>
          </div>
          <button className="border mt-4">Login</button>
        </form>
      </div>
    </>
  );
}
