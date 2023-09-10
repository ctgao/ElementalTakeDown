import './footer.scss';

import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
// import { faGithub } from '@fortawesome/free-brands-svg-icons'

import { Col, Row } from 'reactstrap';

const Footer = () => (
  <div className="footer page-content">
    <Row>
      <Col md="12">
        <p>Created by:&nbsp;
          <a href="https://www.linkedin.com/in/ctgao2/" target="_blank" rel="noopener noreferrer">
            Christine Gao
          </a>
          <br />
          Source Code:&nbsp;
          <a href="https://github.com/ctgao/ElementalTakeDown" target="_blank" rel="noopener noreferrer">
{/*             <FontAwesomeIcon icon={faGithub} /> */}
            GitHub
          </a>
        </p>
      </Col>
    </Row>
  </div>
);

export default Footer;
