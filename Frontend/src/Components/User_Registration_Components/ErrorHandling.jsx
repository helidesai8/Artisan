import React from "react";

export function ErrorHandling ({ message })
 {
  return (
    <div className="text-red-500 pt-2">
      {message}
    </div>
  );
};

export default ErrorHandling;
