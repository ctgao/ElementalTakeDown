import React, { useState, useEffect } from 'react';

export const Checkbox = ({ obj, onChange }) => {
  return (
    <>
      <input
        type="checkbox"
        id={`custom-checkbox-${obj.index}`}
        name={obj.name}
        checked={obj.checked}
        onChange={() => onChange({ ...obj, checked: !obj.checked })}
      />
    </>
  );
};

export default Checkbox;
