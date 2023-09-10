import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';

import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row>
{/*       <Col md="3" className="pad"> */}
{/*         <span className="hipster rounded" /> */}
{/*       </Col> */}
      <Col md="9">
        <h2>Welcome to Elemental TakeDown!</h2>
        {account?.login ? (
          <div>
            <Alert color="success">You are logged in as user &quot;{account.login}&quot;.</Alert>
          </div>
        ) : (
          <div>
            <Alert color="warning">
              If you want to
              <span>&nbsp;</span>
              <Link to="/login" className="alert-link">
                sign in
              </Link>
              , you can try the default accounts:
              <br />- Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;) <br />- User (login=&quot;user&quot; and
              password=&quot;user&quot;).
            </Alert>

            <Alert color="warning">
              You don&apos;t have an account yet?&nbsp;
              <Link to="/account/register" className="alert-link">
                Create a new account
              </Link>
            </Alert>
          </div>
        )}
        <p>
          Want to play a complex and customizable Trading Card Game? With Elemental TakeDown, you can do just that!
          The base game consists of a persisting user profile, allowing each person to have their own repository of cards to play from!
          When your account gets created, the account gets 3 starter cards so you can start playing immediately!
          Playing rounds against other users or NPCs (computer-players) can increase your in-game currency which
          you can use to buy templates for customizing your own cards!
          In-game currency can also be used to buy more base cards to expand your repository.
        </p>
        <p>
          How is Elemental TakeDown different from other TCGs?
          As the name implies, there’s an elemental system in place, filled with different elemental reactions that can
          change your damage output. Some reactions have splash damage (hitting multiple enemies for less)
          and others amplify the current damage (hitting a single enemy for more).
          The key to winning in this TCG is to utilize these different reactions to take down your enemy first!
          You can even create your own cards with the templates you own, further increasing the variety from match to match.
        </p>
        <ul>
          <li>
            <a href="http://localhost:8080/viewcharacters.html" target="_blank">View All Cards</a>
          </li>
          <li>
            <a href="http://localhost:8080/addcharactercard.html" target="_blank">Form for Character Creation</a>
          </li>
        </ul>
      </Col>
    </Row>
  );
};

export default Home;
