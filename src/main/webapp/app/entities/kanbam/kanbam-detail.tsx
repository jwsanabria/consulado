import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './kanbam.reducer';
import { IKanbam } from 'app/shared/model/kanbam.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IKanbamDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class KanbamDetail extends React.Component<IKanbamDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { kanbamEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Kanbam [<b>{kanbamEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="size">Size</span>
            </dt>
            <dd>{kanbamEntity.size}</dd>
            <dt>
              <span id="state">State</span>
            </dt>
            <dd>{kanbamEntity.state}</dd>
          </dl>
          <Button tag={Link} to="/entity/kanbam" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/kanbam/${kanbamEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ kanbam }: IRootState) => ({
  kanbamEntity: kanbam.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(KanbamDetail);
